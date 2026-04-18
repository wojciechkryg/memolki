#!/usr/bin/env bash
# Interactive setup checker for the YouTube upload & Google Ads pipeline.
#
# Checks all prerequisites and guides you through missing steps.
#
# Usage:
#   ./scripts/youtube/setup.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

PASS=0
FAIL=0
WARN=0

pass() { echo "  ✅ $1"; PASS=$((PASS + 1)); }
fail() { echo "  ❌ $1"; FAIL=$((FAIL + 1)); }
warn() { echo "  ⚠️  $1"; WARN=$((WARN + 1)); }
info() { echo "     ℹ️  $1"; }

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔧 Memolki YouTube & Google Ads Pipeline Setup"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# ─── 1. Python packages ────────────────────────────────────────────
echo ""
echo "📦 Python packages"

if python3 -c "import googleapiclient" 2>/dev/null; then
    pass "google-api-python-client"
else
    fail "google-api-python-client"
    info "pip3 install google-api-python-client"
fi

if python3 -c "import google_auth_oauthlib" 2>/dev/null; then
    pass "google-auth-oauthlib"
else
    fail "google-auth-oauthlib"
    info "pip3 install google-auth-oauthlib"
fi

if python3 -c "import google.ads.googleads" 2>/dev/null; then
    pass "google-ads (for Google Ads API)"
else
    warn "google-ads (for Google Ads API) — needed for Ads automation"
    info "pip3 install google-ads"
fi

# ─── 2. YouTube API credentials ───────────────────────────────────
echo ""
echo "🔑 YouTube API credentials"

if [ -f "$SCRIPT_DIR/client_secret.json" ]; then
    pass "client_secret.json found"
else
    fail "client_secret.json missing"
    info "Download from Google Cloud Console → APIs & Services → Credentials"
    info "Create OAuth 2.0 Client ID (Desktop app) and save as client_secret.json"
fi

if [ -f "$SCRIPT_DIR/token.json" ]; then
    pass "token.json found (already authenticated)"
else
    warn "token.json not found — will authenticate on first upload"
    info "Run: ./scripts/youtube/upload_videos.sh --dry-run"
    info "A browser window will open for OAuth consent"
fi

# ─── 3. Google Ads API credentials ────────────────────────────────
echo ""
echo "🎯 Google Ads API credentials"

GOOGLE_ADS_YAML="$SCRIPT_DIR/google-ads.yaml"

if [ -f "$GOOGLE_ADS_YAML" ]; then
    pass "google-ads.yaml found"

    check_yaml_field() {
        local field="$1"
        local placeholder="$2"
        local line
        line=$(grep "^${field}:" "$GOOGLE_ADS_YAML" || true)
        if [ -n "$line" ] && ! echo "$line" | grep -q "$placeholder"; then
            return 0
        fi
        return 1
    }

    if check_yaml_field "developer_token" "INSERT_TOKEN_HERE"; then
        pass "developer_token configured"
    else
        fail "developer_token not set in google-ads.yaml"
        info "Get it from Google Ads → Tools → API Center"
    fi

    if check_yaml_field "client_customer_id" "INSERT_ID_HERE"; then
        pass "client_customer_id configured"
    else
        fail "client_customer_id not set in google-ads.yaml"
        info "Your Google Ads account ID (format: 123-456-7890)"
    fi

    if check_yaml_field "refresh_token" "INSERT_TOKEN_HERE"; then
        pass "refresh_token configured"
    else
        fail "refresh_token not set in google-ads.yaml"
        info "Generate with: ./scripts/youtube/generate_refresh_token.sh"
    fi
else
    warn "google-ads.yaml not found — needed for Google Ads automation"
    info "Run this script with --init-ads to create a template"
fi

# ─── 4. Video files ───────────────────────────────────────────────
echo ""
echo "🎬 Video files"

VIDEO_DIR="$HOME/Desktop/memolki_recordings"

if [ -d "$VIDEO_DIR" ]; then
    VIDEO_COUNT=$(find "$VIDEO_DIR" -name "*.mp4" | wc -l | tr -d ' ')
    pass "Videos: $VIDEO_COUNT files in $VIDEO_DIR"
else
    fail "Video directory missing: $VIDEO_DIR"
    info "Run: ./scripts/recording/record_all_videos.sh"
fi

# ─── 5. Upload progress ──────────────────────────────────────────
echo ""
echo "📊 Upload progress"

mapping="$SCRIPT_DIR/video_urls.json"
if [ -f "$mapping" ]; then
    count=$(python3 -c "import json; print(len(json.load(open('$mapping'))))")
    pass "$count videos uploaded"
else
    info "No uploads yet"
fi

# ─── 6. Play Store listing setup ─────────────────────────────────
echo ""
echo "🏪 Play Store listing setup"

SECRETS="$PROJECT_ROOT/secrets.properties"
if [ -f "$SECRETS" ] && grep -q "PLAY_SERVICE_ACCOUNT_PATH" "$SECRETS"; then
    SA_PATH=$(grep "^PLAY_SERVICE_ACCOUNT_PATH" "$SECRETS" | cut -d'=' -f2-)
    SA_PATH="${SA_PATH%\"}"
    SA_PATH="${SA_PATH#\"}"
    SA_PATH="${SA_PATH%\'}"
    SA_PATH="${SA_PATH#\'}"
    SA_PATH="${SA_PATH/#\~/$HOME}"
    if [ -f "$SA_PATH" ]; then
        pass "Play Store service account configured"
    else
        fail "Service account file not found at: $SA_PATH"
    fi
else
    warn "PLAY_SERVICE_ACCOUNT_PATH not set in secrets.properties"
    info "See docs/docs_listing.md for setup instructions"
fi

# ─── Init google-ads.yaml template ───────────────────────────────
if [[ "${1:-}" == "--init-ads" ]]; then
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "📝 Creating google-ads.yaml template..."

    # Extract client_id and client_secret from the YouTube client_secret.json
    if [ -f "$SCRIPT_DIR/client_secret.json" ]; then
        CLIENT_ID=$(python3 -c "import json; d=json.load(open('$SCRIPT_DIR/client_secret.json')); k=list(d.keys())[0]; print(d[k]['client_id'])")
        CLIENT_SECRET=$(python3 -c "import json; d=json.load(open('$SCRIPT_DIR/client_secret.json')); k=list(d.keys())[0]; print(d[k]['client_secret'])")
    else
        CLIENT_ID="INSERT_CLIENT_ID_HERE"
        CLIENT_SECRET="INSERT_CLIENT_SECRET_HERE"
    fi

    cat > "$GOOGLE_ADS_YAML" << EOF
# Google Ads API configuration for Memolki
# Docs: https://developers.google.com/google-ads/api/docs/client-libs/python/configuration

developer_token: INSERT_TOKEN_HERE
client_id: $CLIENT_ID
client_secret: $CLIENT_SECRET
refresh_token: INSERT_TOKEN_HERE
client_customer_id: INSERT_ID_HERE
login_customer_id: INSERT_ID_HERE

# To get the developer_token:
#   1. Sign in to Google Ads (ads.google.com)
#   2. Go to Tools → API Center
#   3. Copy your developer token
#   Note: For test accounts, token works immediately.
#         For production, you need to apply for Basic or Standard access.
#
# To get the client_customer_id:
#   Your Google Ads account number (format: 123-456-7890, without dashes in yaml)
#
# To get the login_customer_id:
#   If using a Manager account, put the manager account ID here.
#   If using a direct account, same as client_customer_id.
#
# To get the refresh_token:
#   ./scripts/youtube/generate_refresh_token.sh
EOF

    echo "✅ Created $GOOGLE_ADS_YAML"
    echo "   Fill in the values above, then run this script again to verify."
fi

# ─── Summary ──────────────────────────────────────────────────────
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📋 Summary: $PASS passed, $FAIL failed, $WARN warnings"

if [ $FAIL -eq 0 ] && [ $WARN -eq 0 ]; then
    echo ""
    echo "🚀 All good! Run the full pipeline with:"
    echo "   ./scripts/youtube/run_pipeline.sh"
elif [ $FAIL -eq 0 ]; then
    echo ""
    echo "🟡 Ready for YouTube uploads. Fix warnings for full automation."
    echo "   ./scripts/youtube/upload_videos.sh --dry-run"
else
    echo ""
    echo "🔴 Fix the issues above before running uploads."
fi
