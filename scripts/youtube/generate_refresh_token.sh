#!/usr/bin/env bash
#
# Generate an OAuth2 refresh token for the Google Ads API.
# Opens a browser for consent, then prints the token to copy into google-ads.yaml.
#
# Prerequisites:
#   - pip3 install google-auth-oauthlib
#   - client_secret.json in scripts/youtube/
#
# Usage:
#   ./scripts/youtube/generate_refresh_token.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
python3 "$SCRIPT_DIR/generate_refresh_token.py" "$@"
