#!/bin/bash
#
# Record promo videos for all flavors and languages.
#
# Prerequisites:
#   - RECORDING_MODE = true
#   - All flavors installed on Pixel 2 emulator:
#     ./gradlew installFruitHalfDebug installVegetableHalfDebug installMammalSideDebug installBirdSideDebug
#
# Usage:
#   ./scripts/record_all_videos.sh              # All flavors, all languages
#   ./scripts/record_all_videos.sh fruit_half   # Single flavor, all languages

set -e

FLAVORS="${1:-fruit_half vegetable_half mammal_side bird_side}"
LOCALES="en ar cs da de el es et fi fr hi hu in it iw ja ko lt lv nl no pl pt ro ru sk sl sv tr uk vi zh"

TOTAL_FLAVORS=$(echo $FLAVORS | wc -w | tr -d ' ')
TOTAL_LOCALES=$(echo $LOCALES | wc -w | tr -d ' ')
TOTAL=$((TOTAL_FLAVORS * TOTAL_LOCALES))
COUNT=0

echo "Recording $TOTAL videos ($TOTAL_FLAVORS flavors × $TOTAL_LOCALES languages)"
echo ""

for flavor in $FLAVORS; do
    echo "═══ Flavor: $flavor ═══"
    for locale in $LOCALES; do
        COUNT=$((COUNT + 1))
        echo "[$COUNT/$TOTAL] $flavor / $locale"
        ./scripts/record_video.sh "$flavor" "$locale"
        echo ""
    done
done

echo "Done! $TOTAL videos recorded."
echo "Output: ~/Desktop/memolki_recordings/"
