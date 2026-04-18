#!/usr/bin/env python3
"""
Uploads memolki promo videos to YouTube as unlisted and saves URLs to a JSON mapping.

Usage:
    python3 scripts/youtube/upload_videos.py                              # All flavors
    python3 scripts/youtube/upload_videos.py --flavor fruit_half           # Single flavor
    python3 scripts/youtube/upload_videos.py --flavor fruit_half --locale en  # Single video
    python3 scripts/youtube/upload_videos.py --dry-run                    # Preview without uploading

Prerequisites:
    pip3 install google-api-python-client google-auth-oauthlib
    OAuth client_secret.json in scripts/youtube/
"""

import argparse
import json
import sys
import time
from pathlib import Path

from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.exceptions import RefreshError
from google.auth.transport.requests import Request
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload
from googleapiclient.errors import HttpError

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent

CLIENT_SECRET_FILE = SCRIPT_DIR / "client_secret.json"
TOKEN_FILE = SCRIPT_DIR / "token.json"

SCOPES = ["https://www.googleapis.com/auth/youtube.upload"]

FLAVORS = ["fruit_half", "vegetable_half", "mammal_side", "bird_side"]

FLAVOR_APP_NAMES = {
    "fruit_half": "fruits",
    "vegetable_half": "vegetables",
    "mammal_side": "mammals",
    "bird_side": "birds",
}

INPUT_DIR = Path.home() / "Desktop" / "memolki_recordings"
MAPPING_FILE = SCRIPT_DIR / "video_urls.json"

CATEGORY_GAMING = "20"

# YouTube API: 10,000 units/day, upload = 1,600 units → ~6 uploads/day on default quota
QUOTA_COST_PER_UPLOAD = 1600
DEFAULT_DAILY_QUOTA = 10000


def authenticate():
    """Authenticate with YouTube Data API v3 via OAuth2."""
    creds = None

    if TOKEN_FILE.exists():
        creds = Credentials.from_authorized_user_file(str(TOKEN_FILE), SCOPES)

    if not creds or not creds.valid:
        refreshed = False
        if creds and creds.expired and creds.refresh_token:
            print("🔄 Refreshing access token...")
            try:
                creds.refresh(Request())
                refreshed = True
            except RefreshError as e:
                print(f"⚠️  Refresh failed ({e}). Falling back to interactive OAuth.")
                creds = None

        if not refreshed:
            if not CLIENT_SECRET_FILE.exists():
                print(f"❌ Missing {CLIENT_SECRET_FILE}")
                print("   Download OAuth client credentials from Google Cloud Console.")
                sys.exit(1)
            print("🔑 Opening browser for OAuth consent...")
            flow = InstalledAppFlow.from_client_secrets_file(str(CLIENT_SECRET_FILE), SCOPES)
            creds = flow.run_local_server(port=0)

        TOKEN_FILE.write_text(creds.to_json())
        print("✅ Token saved")

    return build("youtube", "v3", credentials=creds)


def load_mapping():
    """Load existing URL mapping for resumability."""
    if MAPPING_FILE.exists():
        return json.loads(MAPPING_FILE.read_text())
    return {}


def save_mapping(mapping):
    """Save URL mapping to JSON."""
    MAPPING_FILE.write_text(json.dumps(mapping, indent=2, ensure_ascii=False))


def get_video_files(flavor_filter=None, locale_filter=None):
    """List video files to upload, grouped by flavor."""
    videos = []

    flavors = [flavor_filter] if flavor_filter else FLAVORS

    for flavor in flavors:
        flavor_dir = INPUT_DIR / flavor
        if not flavor_dir.is_dir():
            print(f"⚠️  Skipping {flavor} — no directory at {flavor_dir}")
            continue

        for mp4 in sorted(flavor_dir.glob("*.mp4")):
            locale = mp4.stem
            if locale_filter and locale != locale_filter:
                continue
            videos.append((flavor, locale, mp4))

    return videos


def upload_video(youtube, file_path, title, description, tags):
    """Upload a single video to YouTube as unlisted. Returns video ID."""
    body = {
        "snippet": {
            "title": title,
            "description": description,
            "tags": tags,
            "categoryId": CATEGORY_GAMING,
        },
        "status": {
            "privacyStatus": "unlisted",
            "selfDeclaredMadeForKids": False,
        },
    }

    media = MediaFileUpload(
        str(file_path),
        mimetype="video/mp4",
        resumable=True,
        chunksize=10 * 1024 * 1024,  # 10 MB chunks
    )

    request = youtube.videos().insert(part="snippet,status", body=body, media_body=media)

    response = None
    while response is None:
        status, response = request.next_chunk()
        if status:
            pct = int(status.progress() * 100)
            print(f"   ⬆️  {pct}%", end="\r")

    video_id = response["id"]
    return video_id


def main():
    parser = argparse.ArgumentParser(description="Upload memolki videos to YouTube")
    parser.add_argument("--flavor", choices=FLAVORS, help="Single flavor to upload")
    parser.add_argument("--locale", help="Single locale to upload (e.g. 'en', 'pl')")
    parser.add_argument(
        "--dry-run", action="store_true", help="List videos without uploading"
    )
    parser.add_argument(
        "--quota-limit",
        type=int,
        default=DEFAULT_DAILY_QUOTA,
        help=f"Daily quota limit in units (default: {DEFAULT_DAILY_QUOTA})",
    )
    args = parser.parse_args()

    if not INPUT_DIR.is_dir():
        print(f"❌ Input directory not found: {INPUT_DIR}")
        sys.exit(1)

    # Gather videos
    videos = get_video_files(args.flavor, args.locale)
    if not videos:
        print("❌ No videos found to upload")
        sys.exit(1)

    # Load existing mapping for resumability
    mapping = load_mapping()

    # Filter out already-uploaded
    to_upload = []
    for flavor, locale, path in videos:
        key = f"{flavor}/{locale}"
        if key in mapping:
            print(f"⏭️  {key} — already uploaded ({mapping[key]['url']})")
        else:
            to_upload.append((flavor, locale, path))

    if not to_upload:
        print(f"\n✅ All videos already uploaded!")
        print(f"📄 Mapping: {MAPPING_FILE}")
        return

    # Quota check
    max_uploads = args.quota_limit // QUOTA_COST_PER_UPLOAD
    if len(to_upload) > max_uploads:
        print(
            f"\n⚠️  {len(to_upload)} videos to upload but quota allows ~{max_uploads}/day"
        )
        print(f"   Will upload {max_uploads} now. Re-run tomorrow for the rest.")
        to_upload = to_upload[:max_uploads]

    print(f"\n📱 {len(to_upload)} videos to upload")

    if args.dry_run:
        print("\n🔍 Dry run — would upload:")
        for flavor, locale, path in to_upload:
            size_mb = path.stat().st_size / (1024 * 1024)
            print(f"   {flavor}/{locale}.mp4 ({size_mb:.1f} MB)")
        print(f"\n📊 Estimated quota cost: {len(to_upload) * QUOTA_COST_PER_UPLOAD} units")
        return

    # Authenticate
    youtube = authenticate()

    # Upload
    uploaded = 0
    failed = 0

    for i, (flavor, locale, path) in enumerate(to_upload, 1):
        key = f"{flavor}/{locale}"
        app_name = FLAVOR_APP_NAMES[flavor]
        title = f"memolki \u2022 {app_name} \u2022 {locale}"
        description = (
            f"Promo video for memolki \u2022 {app_name} ({locale}).\n"
            f"Auto-uploaded for Play Store / Google Ads."
        )
        tags = ["memolki", "memory game", flavor.replace("_", " "), locale]

        size_mb = path.stat().st_size / (1024 * 1024)
        print(f"\n[{i}/{len(to_upload)}] 🎬 {key} ({size_mb:.1f} MB)")

        try:
            video_id = upload_video(youtube, path, title, description, tags)
            url = f"https://youtu.be/{video_id}"
            mapping[key] = {"video_id": video_id, "url": url}
            save_mapping(mapping)
            uploaded += 1
            print(f"   ✅ {url}")
        except HttpError as e:
            failed += 1
            error_reason = e.error_details[0]["reason"] if e.error_details else str(e)
            print(f"   ❌ Failed: {error_reason}")

            if "quotaExceeded" in str(e) or "uploadLimitExceeded" in str(e):
                remaining = len(to_upload) - i
                print(f"\n🛑 Upload limit reached. {remaining} videos remaining.")
                print("   Re-run tomorrow to continue.")
                break

            # Brief pause between uploads to be nice to the API
            time.sleep(1)

    print(f"\n{'━' * 40}")
    print(f"📊 Results: {uploaded} uploaded, {failed} failed")
    print(f"📄 Mapping: {MAPPING_FILE}")

    remaining = len(to_upload) - uploaded - failed
    if remaining > 0:
        print(f"⏳ {remaining} remaining — re-run to continue")


if __name__ == "__main__":
    main()
