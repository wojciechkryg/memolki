#!/usr/bin/env bash
#
# Add uploaded YouTube videos as assets to Google Ads App campaigns.
# Reads video YouTube IDs from video_urls.json and creates
# YoutubeVideoAsset resources linked to App campaigns.
#
# Prerequisites:
#   - pip3 install google-ads
#   - google-ads.yaml configured (run setup.sh --init-ads)
#   - Videos uploaded (run upload_videos.sh)
#
# Usage:
#   ./scripts/youtube/add_ads_video_assets.sh                                    # All flavors
#   ./scripts/youtube/add_ads_video_assets.sh --flavor fruit_half                # Single flavor
#   ./scripts/youtube/add_ads_video_assets.sh --dry-run                          # Preview
#   ./scripts/youtube/add_ads_video_assets.sh --list-campaigns                   # Show App campaigns
#   ./scripts/youtube/add_ads_video_assets.sh --campaign-id fruit_half:12345678  # Explicit mapping

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
python3 "$SCRIPT_DIR/add_ads_video_assets.py" "$@"
