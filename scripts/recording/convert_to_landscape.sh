#!/usr/bin/env bash
# Converts vertical (1080x1920) recordings to landscape (1920x1080)
# by centering the video on a solid background of the flavor's theme color.
#
# Usage:
#   ./scripts/recording/convert_to_landscape.sh              # All flavors
#   ./scripts/recording/convert_to_landscape.sh bird_side    # Single flavor
#
# Input:  ~/Desktop/memolki_recordings/{flavor}/{locale}.mp4
# Output: ~/Desktop/memolki_recordings_landscape/{flavor}/{locale}.mp4

set -euo pipefail

if (( BASH_VERSINFO[0] < 4 )); then
    echo "❌ This script requires Bash 4+ (associative arrays)."
    echo "   Current: ${BASH_VERSION}"
    exit 1
fi

INPUT_DIR="$HOME/Desktop/memolki_recordings"
OUTPUT_DIR="$HOME/Desktop/memolki_recordings_landscape"

declare -A FLAVOR_COLORS=(
    ["fruit_half"]="#FFEAA1"
    ["vegetable_half"]="#E6A0A0"
    ["mammal_side"]="#E2BA8B"
    ["bird_side"]="#B1DBE7"
)

FILTER_FLAVOR="${1:-}"

if [ -n "$FILTER_FLAVOR" ] && [ -z "${FLAVOR_COLORS[$FILTER_FLAVOR]+x}" ]; then
    echo "❌ Unknown flavor: $FILTER_FLAVOR"
    echo "Valid flavors: ${!FLAVOR_COLORS[*]}"
    exit 2
fi

TOTAL=0
CONVERTED=0
SKIPPED=0
FAILED=0

for flavor in "${!FLAVOR_COLORS[@]}"; do
    if [ -n "$FILTER_FLAVOR" ] && [ "$flavor" != "$FILTER_FLAVOR" ]; then
        continue
    fi

    color="${FLAVOR_COLORS[$flavor]}"
    flavor_input="$INPUT_DIR/$flavor"
    flavor_output="$OUTPUT_DIR/$flavor"

    if [ ! -d "$flavor_input" ]; then
        echo "⚠️  Skipping $flavor — no input directory"
        continue
    fi

    mkdir -p "$flavor_output"

    for video in "$flavor_input"/*.mp4; do
        [ -f "$video" ] || continue
        filename=$(basename "$video")
        output_file="$flavor_output/$filename"
        TOTAL=$((TOTAL + 1))

        if [ -f "$output_file" ]; then
            echo "⏭️  $flavor/$filename — already exists, skipping"
            SKIPPED=$((SKIPPED + 1))
            continue
        fi

        echo "🎬 Converting $flavor/$filename (bg: $color)..."

        if ffmpeg -y \
            -f lavfi -i "color=c=${color}:s=1920x1080:r=30,format=rgb24" \
            -i "$video" \
            -filter_complex "[1:v]scale=-2:1080,format=rgb24[scaled]; \
                 [0:v][scaled]overlay=(W-w)/2:(H-h)/2:shortest=1,format=yuv420p" \
            -c:v libx264 -preset medium -crf 18 \
            -c:a copy \
            -r 30 \
            "$output_file" \
            -loglevel warning 2>&1; then
            CONVERTED=$((CONVERTED + 1))
            echo "  ✅ Done"
        else
            FAILED=$((FAILED + 1))
            echo "  ❌ Failed"
            rm -f "$output_file"
        fi
    done
done

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Results: $CONVERTED converted, $SKIPPED skipped, $FAILED failed (total: $TOTAL)"
echo "📁 Output: $OUTPUT_DIR"
