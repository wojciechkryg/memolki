#!/usr/bin/env bash
#
# Capture raw screenshots from the memolki app on a Pixel 9 Pro emulator.
#
# Prerequisites:
#   - RECORDING_MODE = true in RecordingModeProvider.kt
#   - App installed on Pixel 9 Pro emulator (1280x2856)
#   - Emulator running and connected via adb
#
# Usage:
#   ./scripts/screenshot/take_screenshots.sh [flavor] [locale]
#
# Screenshots captured (5 total):
#   1. 3×4 Gameplay — 2 matched pairs + 1 revealed card
#   2. Collection — scrolled to top (unlocked cards)
#   3. 5×6 Gameplay — 12 of 15 pairs matched
#   4. Daily Challenge End Game — 3 stars
#   5. Collection — scrolled to bottom (locked cards)
#
# Note: Daily challenge card layout is date-dependent (seed = epochDay).
#       Re-probe card positions if the date changes.

set -e

# ─── Arguments ───────────────────────────────────────────────────

FLAVOR="${1:-fruit_half}"
LOCALE="${2:-en}"

case "$FLAVOR" in
    fruit_half)      PACKAGE="com.wojdor.memolki.fruithalf" ;;
    vegetable_half)  PACKAGE="com.wojdor.memolki.vegetablehalf" ;;
    mammal_side)     PACKAGE="com.wojdor.memolki.mammalside" ;;
    bird_side)       PACKAGE="com.wojdor.memolki.birdside" ;;
    *) echo "Unknown flavor: $FLAVOR"; exit 1 ;;
esac

# ─── Paths ───────────────────────────────────────────────────────

OUTPUT_DIR="${3:-$HOME/Desktop/memolki_screenshots/${FLAVOR}/${LOCALE}}"
mkdir -p "$OUTPUT_DIR"

# ─── Timing ──────────────────────────────────────────────────────

TAP_DURATION=400
DELAY_OVERLAY=1.0
DELAY_MATCH=1.8
DELAY_TRANSITION=0.7

# ─── Helpers ─────────────────────────────────────────────────────

tap()             { adb shell input swipe "$1" "$2" "$1" "$2" "$TAP_DURATION"; }
wait_overlay()    { sleep $DELAY_OVERLAY; }
wait_match()      { sleep $DELAY_MATCH; }
wait_transition() { sleep $DELAY_TRANSITION; }

screencap() {
    local name="$1"
    local device_path="/sdcard/memolki_ss.png"
    adb shell screencap -p "$device_path"
    adb pull "$device_path" "${OUTPUT_DIR}/${name}" > /dev/null
    adb shell rm "$device_path"
    echo "  Captured: $name"
}

# ─── Tap coordinates (Pixel 9 Pro, 1280x2856, immersive) ────────

MENU_PLAY="641 1472"
MENU_COLLECTION="640 1752"
BOARD_3X4="640 970"
BOARD_5X6="640 2042"
BOARD_DAILY="640 2310"
DIALOG_LEAVE="640 1770"

# ═══════════════════════════════════════════════════════════════════
# SETUP
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Setting up..."
adb shell cmd notification set_dnd on > /dev/null 2>&1
adb shell am force-stop "$PACKAGE"
adb shell pm clear "$PACKAGE" > /dev/null
adb shell cmd locale set-app-locales "$PACKAGE" --locales "$LOCALE"
sleep 0.5

echo "[$FLAVOR/$LOCALE] Launching app..."
adb shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1 > /dev/null 2>&1
sleep 5

# ═══════════════════════════════════════════════════════════════════
# 1: 3×4 Gameplay — 2 matched pairs + 1 revealed card
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Screenshot 1: 3×4 Gameplay"
tap $MENU_PLAY; wait_overlay; wait_transition
tap $BOARD_3X4; wait_overlay; wait_transition; sleep 1.0

tap 237 902; wait_overlay; tap 1043 902; wait_match      # Plum (row 1)
tap 1043 1708; wait_overlay; tap 237 2111; wait_match    # Cherry (rows 3-4)
tap 640 1305; sleep 1.0                                  # Reveal raspberry (row 2)
screencap "raw_1.png"

adb shell input keyevent KEYCODE_BACK; sleep 0.7         # Open leave-game dialog
tap $DIALOG_LEAVE; sleep 1.0                             # Confirm leave

# ═══════════════════════════════════════════════════════════════════
# 2: Collection — scrolled to top (unlocked cards)
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Screenshot 2: Collection (unlocked)"
tap $MENU_COLLECTION; wait_overlay; wait_transition; sleep 0.5
adb shell input swipe 640 600 640 2200 300; sleep 0.5
adb shell input swipe 640 600 640 2200 300; sleep 0.5
screencap "raw_2.png"

# ═══════════════════════════════════════════════════════════════════
# 5: Collection — scrolled to bottom (locked cards)
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Screenshot 5: Collection (locked)"
adb shell input swipe 640 2200 640 600 300; sleep 0.5
adb shell input swipe 640 2200 640 600 300; sleep 0.5
adb shell input swipe 640 2200 640 600 300; sleep 0.5
screencap "raw_5.png"

# ═══════════════════════════════════════════════════════════════════
# 3: 5×6 Gameplay — 12 of 15 pairs matched
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Screenshot 3: 5×6 Gameplay"
adb shell input keyevent KEYCODE_BACK; sleep 1.0
tap $MENU_PLAY; wait_overlay; wait_transition
tap $BOARD_5X6; wait_overlay; wait_transition; sleep 1.0

# 12 pairs (probed layout — deterministic with Random(0) + 20 unlocked)
tap 157 902;  wait_overlay; tap 1124 902;  wait_match    # Coconut
tap 399 902;  wait_overlay; tap 641 2112;  wait_match    # Blueberry
tap 641 902;  wait_overlay; tap 1124 1144; wait_match    # Mango
tap 883 902;  wait_overlay; tap 1124 1386; wait_match    # Banana
tap 157 1144; wait_overlay; tap 399 1628;  wait_match    # Strawberry
tap 399 1144; wait_overlay; tap 1124 2112; wait_match    # Lime
tap 641 1144; wait_overlay; tap 883 1386;  wait_match    # Raspberry
tap 883 1144; wait_overlay; tap 883 2112;  wait_match    # Grape
tap 399 1386; wait_overlay; tap 641 1386;  wait_match    # Pineapple
tap 157 1386; wait_overlay; tap 157 1628;  wait_match    # Cherry
tap 883 1628; wait_overlay; tap 1124 1628; wait_match    # Apple
tap 641 1870; wait_overlay; tap 883 1870;  wait_match    # Pear

sleep 0.8
screencap "raw_3.png"

# ═══════════════════════════════════════════════════════════════════
# 4: Daily Challenge End Game — 3 stars
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Screenshot 4: Daily Challenge End Game"
adb shell am force-stop "$PACKAGE"
adb shell pm clear "$PACKAGE" > /dev/null
adb shell cmd locale set-app-locales "$PACKAGE" --locales "$LOCALE"
sleep 0.5
adb shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1 > /dev/null 2>&1
sleep 5
tap $MENU_PLAY; wait_overlay; wait_transition
tap $BOARD_DAILY; wait_overlay; wait_transition; sleep 1.5

# 15 pairs (probed layout — seed fixed to 0 in RECORDING_MODE, stable across dates)
tap 641 1144; wait_overlay; tap 883 1386;  wait_match    # Apple (R2C3+R3C4)
tap 157 1144; wait_overlay; tap 399 1628;  wait_match    # Avocado (R2C1+R4C2)
tap 883 1628; wait_overlay; tap 157 2112;  wait_match    # Banana (R4C4+R6C1)
tap 399 1386; wait_overlay; tap 641 1386;  wait_match    # Blueberry (R3C2+R3C3)
tap 399 1144; wait_overlay; tap 1124 2112; wait_match    # Cherry (R2C2+R6C5)
tap 399 1870; wait_overlay; tap 883 1870;  wait_match    # Grape (R5C2+R5C4)
tap 399 902;  wait_overlay; tap 641 2112;  wait_match    # Lemon (R1C2+R6C3)
tap 883 902;  wait_overlay; tap 1124 1386; wait_match    # Mango (R1C4+R3C5)
tap 1124 1870; wait_overlay; tap 399 2112; wait_match    # Orange (R5C5+R6C2)
tap 883 1144; wait_overlay; tap 883 2112;  wait_match    # Peach (R2C4+R6C4)
tap 1124 1628; wait_overlay; tap 641 1870; wait_match    # Pear (R4C5+R5C3)
tap 157 902;  wait_overlay; tap 1124 902;  wait_match    # Pineapple (R1C1+R1C5)
tap 641 902;  wait_overlay; tap 1124 1144; wait_match    # Raspberry (R1C3+R2C5)
tap 641 1628; wait_overlay; tap 157 1870;  wait_match    # Strawberry (R4C3+R5C1)
tap 157 1386; wait_overlay; tap 157 1628;  wait_match    # Watermelon (R3C1+R4C1)

sleep 2.5
screencap "raw_4.png"

# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] All 5 screenshots captured in: $OUTPUT_DIR"
