#!/usr/bin/env bash
# Reads YouTube URL mapping and writes video-url.txt files into Play Store listing directories.
#
# Usage:
#   ./scripts/youtube/write_video_urls.sh                    # All flavors
#   ./scripts/youtube/write_video_urls.sh fruit_half         # Single flavor
#   ./scripts/youtube/write_video_urls.sh --dry-run          # Preview without writing
#
# Reads from: scripts/youtube/video_urls.json
# Writes to:  app/src/{gradleFlavor}/play/listings/{playLocale}/video-url.txt
#
# After running, push to Play Store with:
#   ./scripts/listing/update_listings.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
MAPPING_FILE="$SCRIPT_DIR/video_urls.json"

DRY_RUN=false
FILTER_FLAVOR=""

for arg in "$@"; do
    case "$arg" in
        --dry-run) DRY_RUN=true ;;
        *)         FILTER_FLAVOR="$arg" ;;
    esac
done

if [ ! -f "$MAPPING_FILE" ]; then
    echo "❌ No mapping file found at $MAPPING_FILE"
    echo "   Run upload_videos.sh first to upload videos."
    exit 1
fi

# ─── Locale mapping: video locale → Play Store locale ───────────────
declare -A LOCALE_MAP=(
    ["ar"]="ar"
    ["cs"]="cs-CZ"
    ["da"]="da-DK"
    ["de"]="de-DE"
    ["el"]="el-GR"
    ["en"]="en-US"
    ["es"]="es-ES"
    ["et"]="et"
    ["fi"]="fi-FI"
    ["fr"]="fr-FR"
    ["hi"]="hi-IN"
    ["hu"]="hu-HU"
    ["in"]="id"
    ["it"]="it-IT"
    ["iw"]="iw-IL"
    ["ja"]="ja-JP"
    ["ko"]="ko-KR"
    ["lt"]="lt"
    ["lv"]="lv"
    ["nl"]="nl-NL"
    ["no"]="no-NO"
    ["pl"]="pl-PL"
    ["pt"]="pt-PT"
    ["ro"]="ro"
    ["ru"]="ru-RU"
    ["sk"]="sk"
    ["sl"]="sl"
    ["sv"]="sv-SE"
    ["tr"]="tr-TR"
    ["uk"]="uk"
    ["vi"]="vi"
    ["zh"]="zh-CN"
)

# ─── Flavor mapping: video flavor → Gradle flavor ──────────────────
declare -A FLAVOR_MAP=(
    ["fruit_half"]="fruitHalf"
    ["vegetable_half"]="vegetableHalf"
    ["mammal_side"]="mammalSide"
    ["bird_side"]="birdSide"
)

WRITTEN=0
SKIPPED=0
MISSING=0

# Parse JSON mapping — each key is "flavor/locale", value has .url
KEYS=$(python3 -c "
import json, sys
data = json.load(open('$MAPPING_FILE'))
for key in sorted(data.keys()):
    print(f\"{key} {data[key]['url']}\")
")

while IFS=' ' read -r key url; do
    flavor="${key%%/*}"
    locale="${key##*/}"

    # Filter by flavor if specified
    if [ -n "$FILTER_FLAVOR" ] && [ "$flavor" != "$FILTER_FLAVOR" ]; then
        continue
    fi

    gradle_flavor="${FLAVOR_MAP[$flavor]:-}"
    play_locale="${LOCALE_MAP[$locale]:-}"

    if [ -z "$gradle_flavor" ]; then
        echo "⚠️  Unknown flavor: $flavor"
        MISSING=$((MISSING + 1))
        continue
    fi

    if [ -z "$play_locale" ]; then
        echo "⚠️  Unknown locale: $locale (no Play Store mapping)"
        MISSING=$((MISSING + 1))
        continue
    fi

    listing_dir="$PROJECT_ROOT/app/src/$gradle_flavor/play/listings/$play_locale"
    video_url_file="$listing_dir/video-url.txt"

    if [ ! -d "$listing_dir" ]; then
        echo "⚠️  No listing directory: $listing_dir"
        MISSING=$((MISSING + 1))
        continue
    fi

    # Check if already set to same URL
    if [ -f "$video_url_file" ] && [ "$(cat "$video_url_file")" = "$url" ]; then
        SKIPPED=$((SKIPPED + 1))
        continue
    fi

    if $DRY_RUN; then
        echo "🔍 Would write: $gradle_flavor/$play_locale/video-url.txt → $url"
    else
        echo "$url" > "$video_url_file"
        echo "✅ $gradle_flavor/$play_locale → $url"
    fi
    WRITTEN=$((WRITTEN + 1))

done <<< "$KEYS"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if $DRY_RUN; then
    echo "🔍 Dry run: $WRITTEN would be written, $SKIPPED unchanged, $MISSING skipped"
else
    echo "📊 Results: $WRITTEN written, $SKIPPED unchanged, $MISSING skipped"
    if [ "$WRITTEN" -gt 0 ]; then
        echo ""
        echo "Next step: push to Play Store with:"
        echo "  ./scripts/listing/update_listings.sh"
    fi
fi
