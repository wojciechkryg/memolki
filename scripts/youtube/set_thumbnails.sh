#!/usr/bin/env bash
#
# Set custom thumbnails on uploaded YouTube videos using feature graphics.
#
# Prerequisites:
#   - pip3 install google-api-python-client google-auth-oauthlib Pillow
#   - OAuth client_secret.json in scripts/youtube/
#   - YouTube account verified (https://www.youtube.com/verify)
#   - Per-locale feature graphics at app/src/<flavor>/play/listings/<locale>/graphics/feature-graphic/1.png
#
# Usage:
#   ./scripts/youtube/set_thumbnails.sh                              # All flavors
#   ./scripts/youtube/set_thumbnails.sh --flavor fruit_half           # Single flavor
#   ./scripts/youtube/set_thumbnails.sh --dry-run                    # Preview

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
python3 "$SCRIPT_DIR/set_thumbnails.py" "$@"
