#!/usr/bin/env python3
"""
Sets custom thumbnails on uploaded YouTube videos.

Reads video IDs from video_urls.json and composes a 720x1280 portrait thumbnail
from the matching locale's 1024x500 feature graphic: per-flavor colored background,
logo+cards centered on top, each pill detected and individually centered below.

Usage:
    python3 scripts/youtube/set_thumbnails.py                    # All flavors
    python3 scripts/youtube/set_thumbnails.py --flavor fruit_half
    python3 scripts/youtube/set_thumbnails.py --dry-run          # Preview
"""

import argparse
import json
import sys
import tempfile
from pathlib import Path

try:
    from google.oauth2.credentials import Credentials
    from google_auth_oauthlib.flow import InstalledAppFlow
    from google.auth.transport.requests import Request
    from googleapiclient.discovery import build
    from googleapiclient.http import MediaFileUpload
    from PIL import Image
except ImportError:
    print("Missing packages. Run: pip3 install google-api-python-client google-auth-oauthlib Pillow")
    sys.exit(1)

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
CLIENT_SECRET_FILE = SCRIPT_DIR / "client_secret.json"
TOKEN_FILE = SCRIPT_DIR / "token.json"
VIDEO_URLS_FILE = SCRIPT_DIR / "video_urls.json"
PROGRESS_FILE = SCRIPT_DIR / "thumbnails_progress.json"

SCOPES = ["https://www.googleapis.com/auth/youtube"]
FLAVORS = ["fruit_half", "vegetable_half", "mammal_side", "bird_side"]

FLAVOR_TO_GRADLE = {
    "fruit_half": "fruitHalf",
    "vegetable_half": "vegetableHalf",
    "mammal_side": "mammalSide",
    "bird_side": "birdSide",
}

LOCALE_TO_PLAY = {
    "ar": "ar", "cs": "cs-CZ", "da": "da-DK", "de": "de-DE",
    "el": "el-GR", "en": "en-US", "es": "es-ES", "et": "et",
    "fi": "fi-FI", "fr": "fr-FR", "hi": "hi-IN", "hu": "hu-HU",
    "in": "id", "it": "it-IT", "iw": "iw-IL", "ja": "ja-JP",
    "ko": "ko-KR", "lt": "lt", "lv": "lv", "nl": "nl-NL",
    "no": "no-NO", "pl": "pl-PL", "pt": "pt-PT", "ro": "ro",
    "ru": "ru-RU", "sk": "sk", "sl": "sl", "sv": "sv-SE",
    "tr": "tr-TR", "uk": "uk", "vi": "vi", "zh": "zh-CN",
}

FLAVOR_COLORS = {
    "fruit_half": "#FFEAA1",
    "vegetable_half": "#E6A0A0",
    "mammal_side": "#E2BA8B",
    "bird_side": "#B1DBE7",
}

def get_youtube_service():
    creds = None
    if TOKEN_FILE.exists():
        creds = Credentials.from_authorized_user_file(str(TOKEN_FILE), SCOPES)

    if creds and creds.expired and creds.refresh_token:
        try:
            creds.refresh(Request())
        except Exception:
            creds = None

    if not creds or not creds.valid:
        if not CLIENT_SECRET_FILE.exists():
            print(f"Missing {CLIENT_SECRET_FILE}")
            sys.exit(1)
        print("Opening browser for OAuth consent...")
        flow = InstalledAppFlow.from_client_secrets_file(str(CLIENT_SECRET_FILE), SCOPES)
        creds = flow.run_local_server(port=0, prompt="consent")

    TOKEN_FILE.write_text(creds.to_json())
    return build("youtube", "v3", credentials=creds)


def get_feature_graphic(flavor, locale):
    """Find the locale-specific feature graphic for a flavor."""
    gradle_flavor = FLAVOR_TO_GRADLE.get(flavor)
    play_locale = LOCALE_TO_PLAY.get(locale)
    if not gradle_flavor or not play_locale:
        return None
    path = PROJECT_ROOT / "app" / "src" / gradle_flavor / "play" / "listings" / play_locale / "graphics" / "feature-graphic" / "1.png"
    return path if path.exists() else None


def resize_to_thumbnail(source_path, flavor):
    """Recompose 1024x500 feature graphic into 720x1280 portrait:
    logo+cards centered on top, each pill detected individually and centered below.
    Pills are left-aligned at x=695 in source; we scan row-by-row to find each pill's
    actual right edge, then re-center horizontally in the output.
    """
    hex_color = FLAVOR_COLORS.get(flavor, "#FFFFFF").lstrip("#")
    color = tuple(int(hex_color[i:i + 2], 16) for i in (0, 2, 4))

    src = Image.open(source_path).convert("RGBA")
    bg_px = src.getpixel((0, 0))[:3]
    px = src.load()

    bands = []
    in_band = False
    start_y = None
    band_rx = 0
    for y in range(85, 415):
        has = False
        rx = 695
        for x in range(680, src.width):
            r, g, b, a = px[x, y]
            if abs(r - bg_px[0]) + abs(g - bg_px[1]) + abs(b - bg_px[2]) > 40:
                has = True
                rx = x
        if has:
            if not in_band:
                start_y = y
                in_band = True
                band_rx = rx
            else:
                band_rx = max(band_rx, rx)
        else:
            if in_band:
                bands.append((start_y, y - 1, 695, band_rx))
                in_band = False
    if in_band:
        bands.append((start_y, 414, 695, band_rx))

    logo = src.crop((60, 85, 670, 415))
    logo_w = 580
    logo_h = int(logo.height * logo_w / logo.width)
    logo_s = logo.resize((logo_w, logo_h), Image.LANCZOS)

    out = Image.new("RGB", (720, 1280), color)
    out.paste(logo_s, ((720 - logo_w) // 2, 140), logo_s)

    pill_scale = 1.5
    gap = int(15 * pill_scale)
    pad = 3
    pill_y = 600
    for y0, y1, l, r in bands:
        pill = src.crop((l - pad, y0 - pad, r + pad + 1, y1 + pad + 1))
        nw = int(pill.width * pill_scale)
        nh = int(pill.height * pill_scale)
        pill_s = pill.resize((nw, nh), Image.LANCZOS)
        out.paste(pill_s, ((720 - nw) // 2, pill_y), pill_s)
        pill_y += nh + gap

    tmp = tempfile.NamedTemporaryFile(suffix=".png", delete=False)
    tmp.close()
    out.save(tmp.name, "PNG")
    return tmp.name


def main():
    parser = argparse.ArgumentParser(description="Set YouTube video thumbnails")
    parser.add_argument("--flavor", choices=FLAVORS, help="Single flavor")
    parser.add_argument("--dry-run", action="store_true", help="Preview without making changes")
    args = parser.parse_args()

    if not VIDEO_URLS_FILE.exists():
        print(f"No video mapping: {VIDEO_URLS_FILE}")
        print("Upload videos first: ./scripts/youtube/upload_videos.sh")
        sys.exit(1)

    video_urls = json.loads(VIDEO_URLS_FILE.read_text())
    flavors = [args.flavor] if args.flavor else FLAVORS

    progress = json.loads(PROGRESS_FILE.read_text()) if PROGRESS_FILE.exists() else {}

    if not args.dry_run:
        youtube = get_youtube_service()

    updated = 0
    skipped = 0
    failed = 0
    rate_limited = False

    for flavor in flavors:
        if rate_limited:
            break

        flavor_videos = {k: v for k, v in video_urls.items() if k.startswith(f"{flavor}/")}
        if not flavor_videos:
            print(f"No videos for {flavor}")
            continue

        print(f"\n{'=' * 50}")
        print(f"{flavor} — {len(flavor_videos)} videos")

        for key, info in sorted(flavor_videos.items()):
            video_id = info["video_id"]
            locale = key.split("/")[1]

            if key in progress:
                skipped += 1
                continue

            feature_graphic = get_feature_graphic(flavor, locale)
            if not feature_graphic:
                print(f"   {locale} — no feature graphic found")
                failed += 1
                continue

            if args.dry_run:
                print(f"   Would set thumbnail: {locale} ({video_id}) from {feature_graphic.name}")
                updated += 1
                continue

            tmp_path = None
            try:
                tmp_path = resize_to_thumbnail(feature_graphic, flavor)
                youtube.thumbnails().set(
                    videoId=video_id,
                    media_body=MediaFileUpload(tmp_path, mimetype="image/png")
                ).execute()
                updated += 1
                progress[key] = True
                PROGRESS_FILE.write_text(json.dumps(progress, indent=2))
                print(f"   {locale} ({video_id}) — done")
            except Exception as e:
                error_msg = str(e)
                if "forbidden" in error_msg.lower() or "verify" in error_msg.lower():
                    print(f"   {locale} — account not verified for custom thumbnails")
                    print("   Verify at: https://www.youtube.com/verify")
                    rate_limited = True
                    break
                if "rateLimitExceeded" in error_msg or "uploadRateLimitExceeded" in error_msg:
                    print(f"\n   Rate limit reached. Re-run later to continue.")
                    rate_limited = True
                    break
                failed += 1
                print(f"   {locale} — failed: {error_msg}")
            finally:
                if tmp_path:
                    Path(tmp_path).unlink(missing_ok=True)

    print(f"\n{'=' * 50}")
    if args.dry_run:
        print(f"Dry run: {updated} would be updated, {skipped} already done")
    else:
        print(f"Results: {updated} updated, {skipped} already done, {failed} failed")


if __name__ == "__main__":
    main()
