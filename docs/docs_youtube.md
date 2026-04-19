# YouTube Video Upload & Play Store / Google Ads Linking

Upload promo videos to YouTube, link them to Play Store listings, and add them to Google Ads App campaigns — fully automated.

## Overview

Videos are recorded in portrait (9:16) and uploaded to YouTube as unlisted. The same videos are used for both Play Store listings and Google Ads campaigns.

| Source | Purpose | Automation |
|--------|---------|------------|
| `~/Desktop/memolki_recordings/` | Play Store listing + Google Ads | YouTube → `video-url.txt` → `update_listings.sh` |

## Quick start

```bash
# Check prerequisites
./scripts/youtube/setup.sh

# Run the full pipeline (dry run first)
./scripts/youtube/run_pipeline.sh --dry-run
./scripts/youtube/run_pipeline.sh
```

The pipeline is **resumable** — re-run daily until all videos are uploaded (quota limited to ~6/day).

## Setup

### 1. Check prerequisites

```bash
./scripts/youtube/setup.sh
```

This checks Python packages, credentials, video files, and upload progress.

### 2. Install Python packages

```bash
pip3 install google-api-python-client google-auth-oauthlib    # YouTube API
pip3 install google-ads                                        # Google Ads API (optional)
```

### 3. YouTube API (required)

- **OAuth client secret** at `scripts/youtube/client_secret.json` (already present)
- **YouTube Data API v3** enabled in [Google Cloud Console](https://console.cloud.google.com/apis/library/youtube.googleapis.com)
- On first run, a browser opens for OAuth consent → token saved to `scripts/youtube/token.json`

### 4. Google Ads API (optional — for automatic ad asset linking)

```bash
# Create config template (auto-fills client_id/secret from YouTube credentials)
./scripts/youtube/setup.sh --init-ads
```

Then fill in `scripts/youtube/google-ads.yaml`:

| Field | Where to get it |
|-------|----------------|
| `developer_token` | Google Ads → Tools → API Center |
| `client_customer_id` | Your Google Ads account ID (e.g. `1234567890`) |
| `login_customer_id` | Manager account ID (or same as client_customer_id) |
| `refresh_token` | `./scripts/youtube/generate_refresh_token.sh` |

> **Note**: Developer tokens for test accounts work immediately. For production accounts, you need to apply for Basic or Standard access (can take days to weeks).

## Usage

### Full pipeline (recommended)

```bash
./scripts/youtube/run_pipeline.sh                    # Everything
./scripts/youtube/run_pipeline.sh --dry-run          # Preview
./scripts/youtube/run_pipeline.sh --skip-ads         # YouTube + Play Store only
./scripts/youtube/run_pipeline.sh --skip-listings    # YouTube + Google Ads only
```

The pipeline runs these steps in order:

1. Upload videos to YouTube
2. Write YouTube URLs to `video-url.txt` listing files
3. Push listings to Play Store (`update_listings.sh`)
4. Add videos as assets to Google Ads App campaigns

### Individual scripts

#### Upload videos to YouTube

```bash
./scripts/youtube/upload_videos.sh                                    # All flavors
./scripts/youtube/upload_videos.sh --flavor fruit_half                # Single flavor
./scripts/youtube/upload_videos.sh --flavor fruit_half --locale en    # Single video
./scripts/youtube/upload_videos.sh --dry-run                          # Preview
```

#### Write YouTube URLs to listing directories

```bash
./scripts/youtube/write_video_urls.sh                    # All flavors
./scripts/youtube/write_video_urls.sh fruit_half         # Single flavor
./scripts/youtube/write_video_urls.sh --dry-run          # Preview
```

Creates `video-url.txt` in each `androidApp/src/{flavor}/play/listings/{locale}/` directory.

#### Add videos to Google Ads campaigns

```bash
./scripts/youtube/add_ads_video_assets.sh                                        # All
./scripts/youtube/add_ads_video_assets.sh --flavor fruit_half                    # Single
./scripts/youtube/add_ads_video_assets.sh --list-campaigns                       # List campaigns
./scripts/youtube/add_ads_video_assets.sh --campaign-id fruit_half:12345678      # Explicit mapping
./scripts/youtube/add_ads_video_assets.sh --dry-run                              # Preview
```

The script auto-matches flavors to campaigns by name (e.g. campaign containing "fruit" → `fruit_half`). Use `--campaign-id` for explicit mapping.

## Output files

| File | Contents | Gitignored |
|------|----------|------------|
| `scripts/youtube/video_urls.json` | `flavor/locale` → YouTube URL | Yes |
| `scripts/youtube/ads_assets_progress.json` | Google Ads asset linking progress | Yes |
| `scripts/youtube/token.json` | YouTube OAuth2 token | Yes |
| `scripts/youtube/google-ads.yaml` | Google Ads API credentials | Yes |

### Mapping format

```json
{
  "fruit_half/en": {
    "video_id": "dQw4w9WgXcQ",
    "url": "https://youtu.be/dQw4w9WgXcQ"
  }
}
```

## Quota

YouTube Data API v3 default quota: **10,000 units/day**. Each upload costs **1,600 units** → ~6 uploads/day.

With 128 total videos (4 flavors × 32 locales), uploading everything takes ~22 days at default quota.

To speed this up:
1. [Request a quota increase](https://support.google.com/youtube/contact/yt_api_form) — requires audit, can take days to weeks
2. All scripts are **resumable** — just re-run daily, they skip already-uploaded videos

## Scripts overview

```
scripts/youtube/
├── setup.sh                    # Check prerequisites, create config templates
├── run_pipeline.sh             # Master script — runs everything in order
├── upload_videos.sh            # Upload videos to YouTube
├── upload_videos.py            # ↳ Python implementation
├── write_video_urls.sh         # Write YouTube URLs to Play Store listing files
├── add_ads_video_assets.sh     # Add YouTube videos as Google Ads campaign assets
├── add_ads_video_assets.py     # ↳ Python implementation
├── generate_refresh_token.sh   # Generate OAuth refresh token for Google Ads API
├── generate_refresh_token.py   # ↳ Python implementation
├── client_secret.json          # OAuth credentials (gitignored)
├── google-ads.yaml             # Google Ads API config (gitignored, created by setup.sh)
├── token.json                  # YouTube OAuth token (gitignored, auto-generated)
├── video_urls.json             # Upload mapping (gitignored, auto-generated)
└── ads_assets_progress.json    # Ads linking progress (gitignored, auto-generated)
```

## Locale mapping

Videos use short locale codes (`en`, `pl`), Play Store uses full codes (`en-US`, `pl-PL`). Mapping handled by `write_video_urls.sh`:

| Video | Play Store | | Video | Play Store |
|-------|------------|-|-------|------------|
| `ar` | `ar` | | `ko` | `ko-KR` |
| `cs` | `cs-CZ` | | `lt` | `lt` |
| `da` | `da-DK` | | `lv` | `lv` |
| `de` | `de-DE` | | `nl` | `nl-NL` |
| `el` | `el-GR` | | `no` | `no-NO` |
| `en` | `en-US` | | `pl` | `pl-PL` |
| `es` | `es-ES` | | `pt` | `pt-PT` |
| `et` | `et` | | `ro` | `ro` |
| `fi` | `fi-FI` | | `ru` | `ru-RU` |
| `fr` | `fr-FR` | | `sk` | `sk` |
| `hi` | `hi-IN` | | `sl` | `sl` |
| `hu` | `hu-HU` | | `sv` | `sv-SE` |
| `in` | `id` | | `tr` | `tr-TR` |
| `it` | `it-IT` | | `uk` | `uk` |
| `iw` | `iw-IL` | | `vi` | `vi` |
| `ja` | `ja-JP` | | `zh` | `zh-CN` |
