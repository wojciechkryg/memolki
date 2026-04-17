#!/usr/bin/env bash
#
# Upload memolki promo videos to YouTube as unlisted and save URLs to a JSON mapping.
#
# Prerequisites:
#   - pip3 install google-api-python-client google-auth-oauthlib
#   - OAuth client_secret.json in scripts/youtube/
#
# Usage:
#   ./scripts/youtube/upload_videos.sh                              # All flavors
#   ./scripts/youtube/upload_videos.sh --flavor fruit_half           # Single flavor
#   ./scripts/youtube/upload_videos.sh --flavor fruit_half --locale en  # Single video
#   ./scripts/youtube/upload_videos.sh --dry-run                    # Preview without uploading

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
python3 "$SCRIPT_DIR/upload_videos.py" "$@"
