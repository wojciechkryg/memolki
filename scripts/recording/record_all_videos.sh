#!/bin/bash
#
# Record promo videos for all flavors and languages.
#
# Prerequisites:
#   - RECORDING_MODE = true
#   - Pixel 2 emulator running and connected via adb
#
# Usage:
#   ./scripts/recording/record_all_videos.sh              # All flavors, all languages
#   ./scripts/recording/record_all_videos.sh fruit_half   # Single flavor, all languages

set -e

FLAVORS="${1:-fruit_half vegetable_half mammal_side bird_side}"
LOCALES="en ar cs da de el es et fi fr hi hu in it iw ja ko lt lv nl no pl pt ro ru sk sl sv tr uk vi zh"

# ─── Install flavors ────────────────────────────────────────────

flavor_to_task() {
    case "$1" in
        fruit_half)      echo "installFruitHalfDebug" ;;
        vegetable_half)  echo "installVegetableHalfDebug" ;;
        mammal_side)     echo "installMammalSideDebug" ;;
        bird_side)       echo "installBirdSideDebug" ;;
    esac
}

INSTALL_TASKS=""
for flavor in $FLAVORS; do
    INSTALL_TASKS="$INSTALL_TASKS $(flavor_to_task "$flavor")"
done

echo "Installing flavors..."
./gradlew $INSTALL_TASKS
echo ""

# ─── Record ─────────────────────────────────────────────────────

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
        ./scripts/recording/record_video.sh "$flavor" "$locale"
        echo ""
    done
done

echo "Done! $TOTAL videos recorded."
echo "Output: ~/Desktop/memolki_recordings/"
