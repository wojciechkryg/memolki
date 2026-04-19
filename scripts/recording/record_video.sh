#!/usr/bin/env bash
#
# Record a promo video for memolki Google Play Store ads.
#
# Prerequisites:
#   - RECORDING_MODE = true in util/provider/RecordingModeProvider.kt
#   - App installed on Pixel 2 emulator (1080x1920, 9:16)
#   - Emulator running and connected via adb
#   - ffmpeg-full installed (brew install ffmpeg-full)
#
# Usage:
#   ./scripts/recording/record_video.sh [flavor] [locale]
#
# Video flow (~30s at 1.5x speed):
#   1. 3x4 board: mismatches → rapid solve all pairs
#   2. End game: coin reward with total coins
#   3. Unlock New Card → Collection → unlock a card
#   4. 5x6 board: struggle with mismatches → blur + logo + "Can you do better?"

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

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
OUTPUT_DIR="$HOME/Desktop/memolki_recordings/${FLAVOR}"
RAW_FILE="${OUTPUT_DIR}/${LOCALE}_raw.mp4"
OUTPUT_FILE="${OUTPUT_DIR}/${LOCALE}.mp4"
DEVICE_FILE="/sdcard/memolki_recording.mp4"
FFMPEG="/opt/homebrew/opt/ffmpeg-full/bin/ffmpeg"
FONT_PRIMARY="${SCRIPT_DIR}/../../androidApp/src/main/res/font/patrickhand_regular.ttf"
FONT_FALLBACK="/System/Library/Fonts/Supplemental/Arial Unicode.ttf"
MUSIC_FILE="${SCRIPT_DIR}/../../androidApp/src/main/res/raw/music_background.ogg"
LOGO_FILE="${SCRIPT_DIR}/../../androidApp/src/main/res/drawable/ic_logo_${FLAVOR}.png"

mkdir -p "$OUTPUT_DIR"

# ─── Timing ──────────────────────────────────────────────────────

TAP_DURATION=400
DELAY_OVERLAY=1.0
DELAY_MATCH=1.8
DELAY_MISMATCH=2.5
DELAY_TRANSITION=0.7
DELAY_ENDGAME=3.0
DELAY_5X6_CARD=1.2

# ─── Post-processing ────────────────────────────────────────────

SPEED=1.5
BLUR_DURATION=3
BLUR_STRENGTH=8
FADE_IN_BLUR=0.4
FADE_IN_TEXT=0.2
TEXT_DELAY=0.5
TEXT_SIZE=100
LOGO_WIDTH=450
CIRCLE_SIZE=600
BADGE_OFFSET=480
TEXT_SHIFT=200

case "$FLAVOR" in
    fruit_half)      CIRCLE_COLOR="0xFFEAA1" ;;
    vegetable_half)  CIRCLE_COLOR="0xE6A0A0" ;;
    mammal_side)     CIRCLE_COLOR="0xE2BA8B" ;;
    bird_side)       CIRCLE_COLOR="0xB1DBE7" ;;
esac

# ─── Helpers ─────────────────────────────────────────────────────

tap()             { adb shell input swipe "$1" "$2" "$1" "$2" "$TAP_DURATION"; }
wait_overlay()    { sleep $DELAY_OVERLAY; }
wait_match()      { sleep $DELAY_MATCH; }
wait_mismatch()   { sleep $DELAY_MISMATCH; }
wait_transition() { sleep $DELAY_TRANSITION; }

enable_demo_mode() {
    adb shell settings put global sysui_demo_allowed 1
    local demo="adb shell am broadcast -a com.android.systemui.demo"
    $demo -e command enter > /dev/null
    $demo -e command clock -e hhmm 1200 > /dev/null
    $demo -e command battery -e plugged false -e level 100 > /dev/null
    $demo -e command network -e wifi show -e level 4 -e fully true > /dev/null
    $demo -e command notifications -e visible false > /dev/null
}

disable_demo_mode() {
    adb shell am broadcast -a com.android.systemui.demo -e command exit > /dev/null
}

get_end_text() {
    # Line breaks at natural phrase boundaries for each language
    case "$1" in
        ar) LINE1="هل تعتقد";           LINE2="أنك تستطيع حلها؟" ;;
        cs) LINE1="Myslíš,";            LINE2="že to vyřešíš?" ;;
        da) LINE1="Tror du, du kan";    LINE2="løse det?" ;;
        de) LINE1="Glaubst du,";        LINE2="du schaffst es?" ;;
        el) LINE1="Νομίζεις";           LINE2="ότι μπορείς;" ;;
        es) LINE1="¿Crees que puedes";  LINE2="resolverlo?" ;;
        et) LINE1="Arvad,";             LINE2="et sa lahendad?" ;;
        fi) LINE1="Luuletko";           LINE2="ratkaisevasi sen?" ;;
        fr) LINE1="Tu penses";          LINE2="le résoudre ?" ;;
        hi) LINE1="क्या तुम इसे";         LINE2="सुलझा सकते हो?" ;;
        hu) LINE1="Szerinted";           LINE2="meg tudod oldani?" ;;
        in) LINE1="Menurutmu bisa";     LINE2="menyelesaikannya?" ;;
        it) LINE1="Pensi di poterlo";   LINE2="risolvere?" ;;
        iw) LINE1="חושב שתצליח";        LINE2="לפתור?" ;;
        ja) LINE1="解けると";              LINE2="思う？" ;;
        ko) LINE1="풀 수 있다고";          LINE2="생각해?" ;;
        lt) LINE1="Manai,";             LINE2="kad išspręsi?" ;;
        lv) LINE1="Domā,";             LINE2="ka atrisināsi?" ;;
        nl) LINE1="Denk je dat je";     LINE2="het oplost?" ;;
        no) LINE1="Tror du at du";      LINE2="klarer det?" ;;
        pl) LINE1="Myślisz,";           LINE2="że rozwiążesz?" ;;
        pt) LINE1="Achas que consegues"; LINE2="resolver?" ;;
        ro) LINE1="Crezi că poți";      LINE2="rezolva?" ;;
        ru) LINE1="Думаешь,";           LINE2="решишь?" ;;
        sk) LINE1="Myslíš,";            LINE2="že to vyriešiš?" ;;
        sl) LINE1="Misliš,";            LINE2="da zmoreš?" ;;
        sv) LINE1="Tror du att du";     LINE2="löser det?" ;;
        tr) LINE1="Çözebileceğini";     LINE2="düşünüyor musun?" ;;
        uk) LINE1="Думаєш,";            LINE2="розвʼяжеш?" ;;
        vi) LINE1="Bạn nghĩ có thể";    LINE2="giải được không?" ;;
        zh) LINE1="你觉得你能";           LINE2="解开吗？" ;;
        *)  LINE1="Think you can";      LINE2="solve it?" ;;
    esac
}

# ─── Tap coordinates (Pixel 2, 1080x1920) ────────────────────────
# All positions mapped via `adb shell uiautomator dump`.
# Card layout is deterministic with fresh Random(0) per injection.

# Menu
MENU_NEW_GAME="540 945"

# Choose board
BOARD_3X4="540 572"
BOARD_5X6="540 1380"

# 3x4 game grid
#   ┌────────────┬────────────┬────────────┐
#   │ plum half  │ apple whole│ plum whole │
#   │ (201,452)  │ (540,452)  │ (879,452)  │
#   ├────────────┼────────────┼────────────┤
#   │apple whole │ raspberry  │raspberry ½ │
#   │ (201,791)  │ (540,791)  │ (879,791)  │
#   ├────────────┼────────────┼────────────┤
#   │kiwi whole  │ apple half │ cherry half│
#   │ (201,1130) │ (540,1130) │ (879,1130) │
#   ├────────────┼────────────┼────────────┤
#   │cherry whole│ peach whole│ kiwi half  │
#   │ (201,1469) │ (540,1469) │ (879,1469) │
#   └────────────┴────────────┴────────────┘

# End game
EG_UNLOCK="540 928"

# Collection coin-unlock card
COLL_UNLOCK="283 938"

# 5x6 game grid (5 cols × 6 rows) — only positions used in Act 6
G5_R1C2="337 451"
G5_R2C3="540 654"
G5_R3C5="946 857"
G5_R4C4="743 1060"
G5_R5C1="134 1263"
G5_R6C4="743 1466"

# ═══════════════════════════════════════════════════════════════════
# RECORDING
# ═══════════════════════════════════════════════════════════════════

echo "[$FLAVOR/$LOCALE] Setting up..."
adb shell cmd notification set_dnd on > /dev/null 2>&1
enable_demo_mode
adb shell am force-stop "$PACKAGE"
adb shell pm clear "$PACKAGE" > /dev/null
adb shell cmd locale set-app-locales "$PACKAGE" --locales "$LOCALE"
sleep 0.5

echo "[$FLAVOR/$LOCALE] Navigating to 3x4..."
adb shell monkey -p "$PACKAGE" -c android.intent.category.LAUNCHER 1 > /dev/null 2>&1
sleep 4
tap $MENU_NEW_GAME; wait_overlay; wait_transition
tap $BOARD_3X4;     wait_overlay; wait_transition
sleep 1.5

echo "[$FLAVOR/$LOCALE] Recording..."
adb shell screenrecord --time-limit 120 "$DEVICE_FILE" &
RECORD_PID=$!
sleep 0.2

# Act 1: Struggle on 3x4
tap 201 452; wait_overlay; tap 540 452; wait_mismatch        # plum half + apple whole
tap 540 791; wait_overlay; tap 201 452; wait_mismatch        # raspberry + plum half

# Act 2: Rapid solve (mixed order — jumps around the board)
tap 540 1469; wait_overlay; tap 879 1469; wait_match         # Peach (bottom)
tap 201 452;  wait_overlay; tap 879 452;  wait_match         # Plum (top)
tap 879 1130; wait_overlay; tap 201 1469; wait_match         # Cherry (cross)
tap 540 791;  wait_overlay; tap 879 791;  wait_match         # Raspberry (middle)
tap 201 1130; wait_overlay; tap 540 1130; wait_match         # Kiwi (middle)
tap 540 452;  wait_overlay; tap 201 791                      # Apple (top-left)

# Act 3: End game → Unlock New Card
sleep $DELAY_ENDGAME
tap $EG_UNLOCK; sleep 1.2

# Act 4: Collection → unlock card
sleep 1.5
tap $COLL_UNLOCK; wait_overlay; sleep 1.0

# Act 5: Back → Menu → 5x6
adb shell input keyevent KEYCODE_BACK; sleep 1.0
tap $MENU_NEW_GAME; sleep 1.0
tap $BOARD_5X6; sleep 1.0

# Act 6: Struggle on 5x6
tap $G5_R2C3; sleep $DELAY_5X6_CARD; tap $G5_R5C1; wait_mismatch
tap $G5_R4C4; sleep $DELAY_5X6_CARD; tap $G5_R2C3; wait_mismatch
tap $G5_R1C2; sleep $DELAY_5X6_CARD; tap $G5_R6C4; wait_mismatch
tap $G5_R3C5; sleep $DELAY_5X6_CARD; tap $G5_R1C2; sleep 2

# Stop recording
kill $RECORD_PID 2>/dev/null || true
sleep 1
adb pull "$DEVICE_FILE" "$RAW_FILE" > /dev/null
adb shell rm "$DEVICE_FILE"
disable_demo_mode

# ═══════════════════════════════════════════════════════════════════
# POST-PROCESSING (1.5x speed + background music + blur ending)
# ═══════════════════════════════════════════════════════════════════

get_end_text "$LOCALE"

# Patrick Hand doesn't support CJK, Arabic, Hebrew, Hindi — use fallback
case "$LOCALE" in
    ar|el|hi|iw|ja|ko|ru|uk|zh) FONT_FILE="$FONT_FALLBACK" ;;
    *)                 FONT_FILE="$FONT_PRIMARY" ;;
esac

RAW_DURATION=$($FFMPEG -i "$RAW_FILE" 2>&1 | grep Duration | sed 's/.*Duration: \([0-9:.]*\).*/\1/' | awk -F: '{print $1*3600+$2*60+$3}')
FAST_DURATION=$(echo "$RAW_DURATION / $SPEED" | bc -l)
BLUR_START=$(echo "$FAST_DURATION - $BLUR_DURATION" | bc -l)
TEXT_START=$(echo "$BLUR_START + $TEXT_DELAY" | bc -l)

BADGE_FILE="${OUTPUT_DIR}/${LOCALE}_badge.png"
CIRCLE_RADIUS=$((CIRCLE_SIZE / 2))
$FFMPEG -y \
  -f lavfi -i "color=c=${CIRCLE_COLOR}:s=${CIRCLE_SIZE}x${CIRCLE_SIZE},format=rgba,geq=r='r(X,Y)':g='g(X,Y)':b='b(X,Y)':a='clip(255*(${CIRCLE_RADIUS}+0.5-hypot(X-${CIRCLE_RADIUS},Y-${CIRCLE_RADIUS})),0,255)'" \
  -i "$LOGO_FILE" \
  -filter_complex "[1:v]scale=${LOGO_WIDTH}:-1[logo];[0:v][logo]overlay=(W-w)/2:(H-h)/2" \
  -frames:v 1 "$BADGE_FILE" -loglevel error

echo "[$FLAVOR/$LOCALE] Post-processing..."
$FFMPEG -y -i "$RAW_FILE" -i "$MUSIC_FILE" -loop 1 -i "$BADGE_FILE" -filter_complex "
  [0:v]setpts=PTS/$SPEED,split=2[main][blur_src];
  [blur_src]boxblur=${BLUR_STRENGTH}:${BLUR_STRENGTH},format=yuva420p,
    fade=t=in:st=${BLUR_START}:d=${FADE_IN_BLUR}:alpha=1[blurred];
  [2:v]format=yuva420p,
    fade=t=in:st=${TEXT_START}:d=${FADE_IN_TEXT}:alpha=1[badge];
  [main][blurred]overlay=format=auto[with_blur];
  [with_blur][badge]overlay=x=(W-w)/2:y=H/2-${BADGE_OFFSET}-h/2:shortest=1,
  drawtext=text='${LINE1}':fontfile='${FONT_FILE}':fontsize=${TEXT_SIZE}:fontcolor=black:
    alpha='if(gte(t\,${TEXT_START})\,min((t-${TEXT_START})/${FADE_IN_TEXT}\,1)\,0)':
    x=(w-text_w)/2:y=(h/2-text_h-10+${TEXT_SHIFT}),
  drawtext=text='${LINE2}':fontfile='${FONT_FILE}':fontsize=${TEXT_SIZE}:fontcolor=black:
    alpha='if(gte(t\,${TEXT_START})\,min((t-${TEXT_START})/${FADE_IN_TEXT}\,1)\,0)':
    x=(w-text_w)/2:y=(h/2+10+${TEXT_SHIFT})[final]
" -af "afade=t=out:st=${BLUR_START}:d=${BLUR_DURATION}" \
  -map "[final]" -map 1:a -shortest \
  "$OUTPUT_FILE" -loglevel error
rm "$RAW_FILE" "$BADGE_FILE"

echo "[$FLAVOR/$LOCALE] Saved: $OUTPUT_FILE"
