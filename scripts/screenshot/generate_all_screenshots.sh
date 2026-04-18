#!/usr/bin/env bash
#
# Generate Play Store listing screenshots for all flavors and locales.
#
# Prerequisites:
#   - RECORDING_MODE = true in util/provider/RecordingModeProvider.kt
#   - Pixel 9 Pro emulator (1280x2856) running and connected via adb
#   - Python 3 + Pillow installed
#
# Usage:
#   ./scripts/screenshot/generate_all_screenshots.sh                    # All 4 flavors × 32 locales
#   ./scripts/screenshot/generate_all_screenshots.sh fruit_half         # All locales for one flavor
#   ./scripts/screenshot/generate_all_screenshots.sh fruit_half en,pl   # Specific locales

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# ─── Preflight: RECORDING_MODE must be true ────────────────────────
RECORDING_MODE_FILE="$PROJECT_ROOT/app/src/main/java/com/wojdor/memolki/util/provider/RecordingModeProvider.kt"
if ! grep -q "RECORDING_MODE = true" "$RECORDING_MODE_FILE"; then
    echo "✗ RECORDING_MODE is not true in $RECORDING_MODE_FILE"
    echo "  Boards won't be seeded and tap coordinates will miss cards."
    echo "  Set RECORDING_MODE = true, then re-run this script (it will install the updated APK)."
    exit 1
fi
echo "✓ RECORDING_MODE = true confirmed"

# Flavor → Gradle install task name
flavor_to_task() {
    case "$1" in
        fruit_half)      echo "installFruitHalfDebug" ;;
        vegetable_half)  echo "installVegetableHalfDebug" ;;
        mammal_side)     echo "installMammalSideDebug" ;;
        bird_side)       echo "installBirdSideDebug" ;;
    esac
}

FLAVORS="${1:-fruit_half mammal_side bird_side}"
DEFAULT_LOCALES="en ar cs da de el es et fi fr hi hu in it iw ja ko lt lv nl no pl pt ro ru sk sl sv tr uk vi zh"
LOCALES="${2:-$DEFAULT_LOCALES}"
# Support comma-separated locales: "en,pl,de" → "en pl de"
LOCALES="${LOCALES//,/ }"

RAW_BASE="$HOME/Desktop/memolki_screenshots"

TOTAL=0
for flavor in $FLAVORS; do
    for locale in $LOCALES; do
        TOTAL=$((TOTAL + 1))
    done
done

COUNT=0
for flavor in $FLAVORS; do
    echo ""
    echo "═══════════════════════════════════════════════════════════════"
    echo "Installing $flavor..."
    echo "═══════════════════════════════════════════════════════════════"
    TASK=$(flavor_to_task "$flavor")
    "$PROJECT_ROOT/gradlew" -p "$PROJECT_ROOT" "$TASK" -q

    for locale in $LOCALES; do
        COUNT=$((COUNT + 1))
        echo ""
        echo "═══════════════════════════════════════════════════════════════"
        echo "[$COUNT/$TOTAL] $flavor / $locale"
        echo "═══════════════════════════════════════════════════════════════"

        RAW_DIR="${RAW_BASE}/${flavor}/${locale}"

        # Capture raw screenshots from emulator
        "$SCRIPT_DIR/take_screenshots.sh" "$flavor" "$locale" "$RAW_DIR"

        # Compose final screenshots with device frames and text
        python3 "$SCRIPT_DIR/compose_screenshots.py" "$flavor" "$locale" "$RAW_DIR"

        echo "[$flavor/$locale] Complete ✓"
    done
done

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "All done! Generated $TOTAL × 5 = $((TOTAL * 5)) screenshots."
echo "═══════════════════════════════════════════════════════════════"
