#!/usr/bin/env bash
# Master pipeline: upload videos → write listing URLs → push to Play Store + add to Google Ads.
#
# Usage:
#   ./scripts/youtube/run_pipeline.sh                    # Full pipeline
#   ./scripts/youtube/run_pipeline.sh --dry-run          # Preview all steps
#   ./scripts/youtube/run_pipeline.sh --skip-ads         # Skip Google Ads step
#   ./scripts/youtube/run_pipeline.sh --skip-listings    # Skip Play Store listing push

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

DRY_RUN=""
SKIP_ADS=false
SKIP_LISTINGS=false
EXTRA_ARGS=""

for arg in "$@"; do
    case "$arg" in
        --dry-run)         DRY_RUN="--dry-run" ;;
        --skip-ads)        SKIP_ADS=true ;;
        --skip-listings)   SKIP_LISTINGS=true ;;
        *)                 EXTRA_ARGS="$EXTRA_ARGS $arg" ;;
    esac
done

step() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "📌 Step $1: $2"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

STEP=0

# ─── Step 1: Upload videos to YouTube ───────────────────────────
STEP=$((STEP + 1))
step $STEP "Upload videos to YouTube"
"$SCRIPT_DIR/upload_videos.sh" $DRY_RUN $EXTRA_ARGS

# ─── Step 2: Write YouTube URLs to listing files ────────────────
if ! $SKIP_LISTINGS; then
    VIDEO_MAPPING="$SCRIPT_DIR/video_urls.json"
    if [ -f "$VIDEO_MAPPING" ] || [ -n "$DRY_RUN" ]; then
        STEP=$((STEP + 1))
        step $STEP "Write video URLs to Play Store listing files"
        if [ -n "$DRY_RUN" ]; then
            if [ -f "$VIDEO_MAPPING" ]; then
                bash "$SCRIPT_DIR/write_video_urls.sh" --dry-run
            else
                echo "   ⏭️  No video mapping yet — skipping (dry run)"
            fi
        else
            bash "$SCRIPT_DIR/write_video_urls.sh"
        fi
    else
        echo ""
        echo "⏭️  No uploads yet — skipping URL writing"
    fi
fi

# ─── Step 3: Push listings to Play Store ────────────────────────
if ! $SKIP_LISTINGS; then
    STEP=$((STEP + 1))
    step $STEP "Push listings to Play Store"
    if [ -n "$DRY_RUN" ]; then
        echo "   🔍 Would run: ./scripts/listing/update_listings.sh"
    else
        "$PROJECT_ROOT/scripts/listing/update_listings.sh"
    fi
fi

# ─── Step 4: Add videos to Google Ads campaigns ────────────────
if ! $SKIP_ADS; then
    ADS_CONFIG="$SCRIPT_DIR/google-ads.yaml"
    VIDEO_MAPPING="$SCRIPT_DIR/video_urls.json"

    if [ -f "$ADS_CONFIG" ] && [ -f "$VIDEO_MAPPING" ]; then
        STEP=$((STEP + 1))
        step $STEP "Add videos to Google Ads campaigns"
        "$SCRIPT_DIR/add_ads_video_assets.sh" $DRY_RUN $EXTRA_ARGS
    elif [ ! -f "$ADS_CONFIG" ]; then
        echo ""
        echo "⏭️  Skipping Google Ads — google-ads.yaml not configured"
        echo "   Run: ./scripts/youtube/setup.sh --init-ads"
    else
        echo ""
        echo "⏭️  No uploads yet — skipping Google Ads"
    fi
fi

# ─── Summary ──────────────────────────────────────────────────────
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
if [ -n "$DRY_RUN" ]; then
    echo "🔍 Dry run complete — no changes made"
else
    echo "✅ Pipeline complete!"
    echo ""
    echo "💡 Due to YouTube quota limits (~6 uploads/day), you may need to"
    echo "   re-run this script daily until all videos are uploaded."
    echo "   The script resumes where it left off automatically."
fi
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
