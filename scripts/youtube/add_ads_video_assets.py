#!/usr/bin/env python3
"""
Adds uploaded YouTube videos as assets to Google Ads App campaigns.

Reads video YouTube IDs from video_urls.json and creates
YoutubeVideoAsset resources linked to App campaigns.

Usage:
    python3 scripts/youtube/add_ads_video_assets.py                    # All flavors
    python3 scripts/youtube/add_ads_video_assets.py --flavor fruit_half
    python3 scripts/youtube/add_ads_video_assets.py --dry-run          # Preview
    python3 scripts/youtube/add_ads_video_assets.py --list-campaigns   # Show App campaigns

Prerequisites:
    pip3 install google-ads
    google-ads.yaml configured (run setup.sh --init-ads)
"""

import argparse
import json
import sys
from pathlib import Path

try:
    from google.ads.googleads.client import GoogleAdsClient
    from google.ads.googleads.errors import GoogleAdsException
except ImportError:
    print("❌ google-ads package not installed")
    print("   pip3 install google-ads")
    sys.exit(1)

SCRIPT_DIR = Path(__file__).resolve().parent
GOOGLE_ADS_YAML = SCRIPT_DIR / "google-ads.yaml"
VIDEO_URLS_FILE = SCRIPT_DIR / "video_urls.json"
ADS_PROGRESS_FILE = SCRIPT_DIR / "ads_assets_progress.json"

FLAVORS = ["fruit_half", "vegetable_half", "mammal_side", "bird_side"]

# Map video flavor names to expected App campaign name patterns.
# Update these if your campaign names differ.
FLAVOR_CAMPAIGN_PATTERNS = {
    "fruit_half": "fruit",
    "vegetable_half": "vegetable",
    "mammal_side": "mammal",
    "bird_side": "bird",
}


def get_client():
    """Load Google Ads client from yaml config."""
    if not GOOGLE_ADS_YAML.exists():
        print(f"❌ Missing {GOOGLE_ADS_YAML}")
        print("   Run: ./scripts/youtube/setup.sh --init-ads")
        sys.exit(1)
    return GoogleAdsClient.load_from_storage(str(GOOGLE_ADS_YAML))


def get_customer_id(client):
    """Read customer ID from config."""
    return client.login_customer_id or client.linked_customer_id


def list_app_campaigns(client, customer_id):
    """List all App campaigns in the account."""
    ga_service = client.get_service("GoogleAdsService")
    query = """
        SELECT campaign.id,
               campaign.name,
               campaign.status,
               campaign.advertising_channel_type
        FROM campaign
        WHERE campaign.advertising_channel_type = 'MULTI_CHANNEL'
        AND campaign.status != 'REMOVED'
        ORDER BY campaign.name
    """
    response = ga_service.search(customer_id=customer_id, query=query)
    campaigns = []
    for row in response:
        campaigns.append({
            "id": row.campaign.id,
            "name": row.campaign.name,
            "status": row.campaign.status.name,
            "resource_name": row.campaign.resource_name,
        })
    return campaigns


def find_campaign_for_flavor(campaigns, flavor):
    """Find the App campaign matching a flavor by name pattern."""
    pattern = FLAVOR_CAMPAIGN_PATTERNS.get(flavor, flavor)
    for campaign in campaigns:
        if pattern.lower() in campaign["name"].lower():
            return campaign
    return None


def get_existing_video_assets(client, customer_id):
    """Get YouTube video IDs already added as assets."""
    ga_service = client.get_service("GoogleAdsService")
    query = """
        SELECT asset.youtube_video_asset.youtube_video_id,
               asset.name,
               asset.resource_name
        FROM asset
        WHERE asset.type = 'YOUTUBE_VIDEO'
    """
    existing = set()
    try:
        response = ga_service.search(customer_id=customer_id, query=query)
        for row in response:
            vid = row.asset.youtube_video_asset.youtube_video_id
            if vid:
                existing.add(vid)
    except GoogleAdsException:
        pass  # No video assets yet
    return existing


def create_video_asset(client, customer_id, video_id, asset_name):
    """Create a YoutubeVideoAsset. Returns the asset resource name."""
    asset_service = client.get_service("AssetService")
    asset_operation = client.get_type("AssetOperation")

    asset = asset_operation.create
    asset.name = asset_name
    asset.youtube_video_asset.youtube_video_id = video_id

    response = asset_service.mutate_assets(
        customer_id=customer_id, operations=[asset_operation]
    )
    return response.results[0].resource_name


def link_asset_to_campaign(client, customer_id, campaign_resource_name, asset_resource_name):
    """Link a video asset to a campaign."""
    campaign_asset_service = client.get_service("CampaignAssetService")
    campaign_asset_operation = client.get_type("CampaignAssetOperation")

    campaign_asset = campaign_asset_operation.create
    campaign_asset.campaign = campaign_resource_name
    campaign_asset.asset = asset_resource_name
    campaign_asset.field_type = client.enums.AssetFieldTypeEnum.YOUTUBE_VIDEO

    response = campaign_asset_service.mutate_campaign_assets(
        customer_id=customer_id, operations=[campaign_asset_operation]
    )
    return response.results[0].resource_name


def load_progress():
    """Load progress of already-linked assets for resumability."""
    if ADS_PROGRESS_FILE.exists():
        return json.loads(ADS_PROGRESS_FILE.read_text())
    return {}


def save_progress(progress):
    """Save progress to JSON."""
    ADS_PROGRESS_FILE.write_text(json.dumps(progress, indent=2))


def main():
    parser = argparse.ArgumentParser(description="Add YouTube videos to Google Ads campaigns")
    parser.add_argument("--flavor", choices=FLAVORS, help="Single flavor")
    parser.add_argument("--dry-run", action="store_true", help="Preview without making changes")
    parser.add_argument("--list-campaigns", action="store_true", help="List App campaigns and exit")
    parser.add_argument(
        "--campaign-id",
        type=str,
        action="append",
        help="Explicit campaign ID to use for a flavor (format: flavor:campaign_id, e.g. fruit_half:12345678). Repeat for multiple flavors.",
    )
    args = parser.parse_args()

    client = get_client()
    customer_id = client.login_customer_id

    if not customer_id:
        print("❌ client_customer_id not set in google-ads.yaml")
        sys.exit(1)

    # Remove dashes from customer ID
    customer_id = customer_id.replace("-", "")

    # ─── List campaigns mode ──────────────────────────────────────
    if args.list_campaigns:
        print("🎯 App campaigns in your account:\n")
        campaigns = list_app_campaigns(client, customer_id)
        if not campaigns:
            print("   No App campaigns found.")
            print("   Note: App campaigns show as MULTI_CHANNEL type.")
        else:
            for c in campaigns:
                print(f"   [{c['id']}] {c['name']} ({c['status']})")
        return

    # ─── Load video mapping ─────────────────────────────────────────
    if not VIDEO_URLS_FILE.exists():
        print(f"❌ No video URL mapping: {VIDEO_URLS_FILE}")
        print("   Upload videos first:")
        print("   ./scripts/youtube/upload_videos.sh")
        sys.exit(1)

    video_urls = json.loads(VIDEO_URLS_FILE.read_text())

    # ─── Parse explicit campaign mappings ─────────────────────────
    explicit_campaigns = {}
    if args.campaign_id:
        for mapping in args.campaign_id:
            parts = mapping.split(":")
            if len(parts) != 2:
                print(f"❌ Invalid --campaign-id format: {mapping}")
                print("   Use: --campaign-id fruit_half:12345678")
                sys.exit(1)
            explicit_campaigns[parts[0]] = parts[1]

    # ─── Find campaigns ──────────────────────────────────────────
    if not args.dry_run:
        campaigns = list_app_campaigns(client, customer_id)
        existing_video_ids = get_existing_video_assets(client, customer_id)
    else:
        campaigns = []
        existing_video_ids = set()

    progress = load_progress()
    flavors = [args.flavor] if args.flavor else FLAVORS

    created = 0
    linked = 0
    skipped = 0
    failed = 0

    for flavor in flavors:
        # Collect videos for this flavor
        flavor_videos = {
            k: v for k, v in video_urls.items() if k.startswith(f"{flavor}/")
        }
        if not flavor_videos:
            print(f"⚠️  No videos for {flavor}")
            continue

        # Find campaign
        campaign = None
        if flavor in explicit_campaigns:
            campaign_id = explicit_campaigns[flavor]
            for c in campaigns:
                if str(c["id"]) == campaign_id:
                    campaign = c
                    break
            if not campaign and not args.dry_run:
                print(f"❌ Campaign ID {campaign_id} not found for {flavor}")
                failed += len(flavor_videos)
                continue
        elif not args.dry_run:
            campaign = find_campaign_for_flavor(campaigns, flavor)
            if not campaign:
                print(f"⚠️  No matching App campaign for {flavor}")
                print(f"   Available campaigns:")
                for c in campaigns:
                    print(f"     [{c['id']}] {c['name']}")
                print(f"   Use: --campaign-id {flavor}:CAMPAIGN_ID")
                failed += len(flavor_videos)
                continue

        campaign_name = campaign["name"] if campaign else "(dry-run)"
        print(f"\n{'═' * 50}")
        print(f"🎮 {flavor} → campaign: {campaign_name}")
        print(f"   {len(flavor_videos)} videos")

        for key, info in sorted(flavor_videos.items()):
            video_id = info["video_id"]
            locale = key.split("/")[1]
            progress_key = f"{flavor}/{locale}"

            if progress_key in progress:
                skipped += 1
                continue

            asset_name = f"memolki_{flavor}_{locale}"

            if args.dry_run:
                print(f"   🔍 Would add: {locale} ({video_id})")
                created += 1
                continue

            try:
                # Create asset (skip if video already exists as asset)
                if video_id in existing_video_ids:
                    print(f"   ⏭️  {locale} — asset already exists")
                    # Still need to link to campaign
                    # Find the existing asset resource name
                    ga_service = client.get_service("GoogleAdsService")
                    query = f"""
                        SELECT asset.resource_name
                        FROM asset
                        WHERE asset.youtube_video_asset.youtube_video_id = '{video_id}'
                        LIMIT 1
                    """
                    response = ga_service.search(customer_id=customer_id, query=query)
                    asset_rn = None
                    for row in response:
                        asset_rn = row.asset.resource_name
                    if not asset_rn:
                        print(f"   ❌ {locale} — could not find existing asset")
                        failed += 1
                        continue
                    newly_created = False
                else:
                    asset_rn = create_video_asset(client, customer_id, video_id, asset_name)
                    existing_video_ids.add(video_id)
                    newly_created = True
                    print(f"   ✅ {locale} — asset created")

                # Link to campaign
                link_asset_to_campaign(
                    client, customer_id, campaign["resource_name"], asset_rn
                )
                if newly_created:
                    created += 1
                linked += 1
                print(f"   🔗 {locale} — linked to campaign")

                progress[progress_key] = {
                    "video_id": video_id,
                    "asset_resource_name": asset_rn,
                    "campaign_id": campaign["id"],
                }
                save_progress(progress)

            except GoogleAdsException as e:
                failed += 1
                for error in e.failure.errors:
                    print(f"   ❌ {locale} — {error.message}")

    print(f"\n{'━' * 50}")
    if args.dry_run:
        print(f"🔍 Dry run: {created} would be created, {skipped} already done")
    else:
        print(f"📊 Results: {created} assets created, {linked} linked, {skipped} skipped, {failed} failed")
        if ADS_PROGRESS_FILE.exists():
            print(f"📄 Progress: {ADS_PROGRESS_FILE}")


if __name__ == "__main__":
    main()
