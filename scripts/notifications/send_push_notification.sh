#!/bin/bash
set -euo pipefail

# Send FCM push notifications to memolki users by language (and optionally by flavor).
#
# Prerequisites:
#   - gcloud CLI authenticated with Firebase project access
#     (run: gcloud auth login && gcloud config set project <firebase-project-id>)
#
# Usage:
#   ./scripts/notifications/send_push_notification.sh <translations_file> [--flavor <flavor>] [--scheduled] [--screen <screen>] [--board <level>]
#
# Translations file format (one language per line, tab-separated):
#   en	New card deck!	Check out the new animals theme
#   pl	Nowa talia kart!	Sprawdź nowy motyw ze zwierzętami
#   de	Neues Kartendeck!	Entdecke das neue Tierthema
#
# Screen options: shop, collection, more_apps, game, daily_challenge
# Level options (only for game screen): 2x3, 3x4, 4x4, 4x5, 4x6, 5x6
#
# Examples:
#   ./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt
#   ./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt --screen shop
#   ./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt --screen game --board 4x5
#   ./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt --flavor fruithalf --scheduled

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SECRETS_FILE="$SCRIPT_DIR/../../secrets.properties"

if [ -f "$SECRETS_FILE" ]; then
    FIREBASE_PROJECT_ID=$(grep '^FIREBASE_PROJECT_ID=' "$SECRETS_FILE" | sed 's/^FIREBASE_PROJECT_ID=//' | tr -d '"')
fi

if [ -z "${FIREBASE_PROJECT_ID:-}" ]; then
    echo "Error: FIREBASE_PROJECT_ID not set. Add it to secrets.properties or export as env variable."
    exit 1
fi

SUPPORTED_LANGUAGES=(
    ar cs da de el en es et fi fr hi hu in it iw ja ko lt lv nl no pl pt ro ru sk sl sv tr uk vi zh
)

WINDOW_START=14
WINDOW_END=21

usage() {
    echo "Usage: $0 <translations_file> [--flavor <flavor>] [--scheduled] [--screen <screen>] [--board <level>]"
    echo ""
    echo "Translations file format (tab-separated):"
    echo "  en	Title text	Body text"
    echo "  pl	Tytuł	Treść"
    echo ""
    echo "Options:"
    echo "  --flavor <flavor>      Target specific flavor (fruithalf, vegetablehalf, mammalside, birdside)"
    echo "  --screen <screen>      Deep link target: shop, collection, more_apps, game, daily_challenge"
    echo "  --board <level>        Game board: 2x3, 3x4, 4x4, 4x5, 4x6, 5x6 (only with --screen game)"
    echo "  --scheduled            Send between ${WINDOW_START}:00-${WINDOW_END}:00 in each language's timezone"
    echo ""
    echo "Config:"
    echo "  FIREBASE_PROJECT_ID is read from secrets.properties (or env variable)"
    exit 1
}

if [ $# -lt 1 ]; then
    usage
fi

TRANSLATIONS_FILE="$1"
shift

FLAVOR=""
SCREEN=""
BOARD=""
SCHEDULED=false
while [ $# -gt 0 ]; do
    case "$1" in
        --flavor)
            FLAVOR="$2"
            shift 2
            ;;
        --screen)
            SCREEN="$2"
            shift 2
            ;;
        --board)
            BOARD="$2"
            shift 2
            ;;
        --scheduled)
            SCHEDULED=true
            shift
            ;;
        *)
            echo "Unknown option: $1"
            usage
            ;;
    esac
done

if [ ! -f "$TRANSLATIONS_FILE" ]; then
    echo "Error: Translations file not found: $TRANSLATIONS_FILE"
    exit 1
fi

FCM_URL="https://fcm.googleapis.com/v1/projects/${FIREBASE_PROJECT_ID}/messages:send"

get_timezone_for_language() {
    case "$1" in
        ar) echo "Asia/Riyadh" ;;
        cs) echo "Europe/Prague" ;;
        da) echo "Europe/Copenhagen" ;;
        de) echo "Europe/Berlin" ;;
        el) echo "Europe/Athens" ;;
        en) echo "America/New_York" ;;
        es) echo "Europe/Madrid" ;;
        et) echo "Europe/Tallinn" ;;
        fi) echo "Europe/Helsinki" ;;
        fr) echo "Europe/Paris" ;;
        hi) echo "Asia/Kolkata" ;;
        hu) echo "Europe/Budapest" ;;
        in) echo "Asia/Jakarta" ;;
        it) echo "Europe/Rome" ;;
        iw) echo "Asia/Jerusalem" ;;
        ja) echo "Asia/Tokyo" ;;
        ko) echo "Asia/Seoul" ;;
        lt) echo "Europe/Vilnius" ;;
        lv) echo "Europe/Riga" ;;
        nl) echo "Europe/Amsterdam" ;;
        no) echo "Europe/Oslo" ;;
        pl) echo "Europe/Warsaw" ;;
        pt) echo "Europe/Lisbon" ;;
        ro) echo "Europe/Bucharest" ;;
        ru) echo "Europe/Moscow" ;;
        sk) echo "Europe/Bratislava" ;;
        sl) echo "Europe/Ljubljana" ;;
        sv) echo "Europe/Stockholm" ;;
        tr) echo "Europe/Istanbul" ;;
        uk) echo "Europe/Kyiv" ;;
        vi) echo "Asia/Ho_Chi_Minh" ;;
        zh) echo "Asia/Shanghai" ;;
        *)  echo "UTC" ;;
    esac
}

# Returns seconds to wait before sending for a given timezone.
# 0 = send now (already in window), >0 = wait this many seconds.
calculate_delay() {
    local tz="$1"
    python3 -c "
from datetime import datetime, timedelta
from zoneinfo import ZoneInfo
import random

tz = ZoneInfo('$tz')
now = datetime.now(tz)
today_start = now.replace(hour=$WINDOW_START, minute=0, second=0, microsecond=0)
today_end = now.replace(hour=$WINDOW_END, minute=0, second=0, microsecond=0)

if today_start <= now < today_end:
    print(0)
elif now < today_start:
    random_offset = random.randint(0, ($WINDOW_END - $WINDOW_START) * 3600 - 1)
    target = today_start + timedelta(seconds=random_offset)
    print(int((target - now).total_seconds()))
else:
    tomorrow_start = today_start + timedelta(days=1)
    random_offset = random.randint(0, ($WINDOW_END - $WINDOW_START) * 3600 - 1)
    target = tomorrow_start + timedelta(seconds=random_offset)
    print(int((target - now).total_seconds()))
"
}

format_send_time() {
    local tz="$1"
    local delay="$2"
    python3 -c "
from datetime import datetime, timedelta
from zoneinfo import ZoneInfo
tz = ZoneInfo('$tz')
send_time = datetime.now(tz) + timedelta(seconds=$delay)
print(send_time.strftime('%H:%M %Z'))
"
}

send_notification() {
    local lang="$1"
    local title="$2"
    local body="$3"

    local access_token
    access_token=$(gcloud auth print-access-token 2>/dev/null) || {
        echo "Failed [$lang]: Could not get access token"
        return 1
    }

    local condition
    if [ -n "$FLAVOR" ]; then
        condition="'lang_${lang}' in topics && '${FLAVOR}' in topics"
    else
        condition="'lang_${lang}' in topics"
    fi

    local data_fields="\"title\": $(printf '%s' "$title" | python3 -c 'import json,sys; print(json.dumps(sys.stdin.read()))'), \"body\": $(printf '%s' "$body" | python3 -c 'import json,sys; print(json.dumps(sys.stdin.read()))')"
    if [ -n "$SCREEN" ]; then
        data_fields="$data_fields, \"screen\": \"$SCREEN\""
        if [ -n "$BOARD" ]; then
            data_fields="$data_fields, \"board\": \"$BOARD\""
        fi
    fi

    local payload
    payload=$(cat <<EOF
{
  "message": {
    "condition": "$condition",
    "data": { ${data_fields} }
  }
}
EOF
    )

    local response
    response=$(curl -s -w "\n%{http_code}" -X POST "$FCM_URL" \
        -H "Authorization: Bearer $access_token" \
        -H "Content-Type: application/json" \
        -d "$payload")

    local http_code
    http_code=$(echo "$response" | tail -1)

    if [ "$http_code" = "200" ]; then
        echo "Sent [$lang]: $title"
    else
        local response_body
        response_body=$(echo "$response" | sed '$d')
        echo "Failed [$lang] (HTTP $http_code): $response_body"
    fi
}

send_count=0
fail_count=0
scheduled_count=0
pids=()

while IFS=$'\t' read -r lang title body; do
    [ -z "$lang" ] && continue
    [[ "$lang" == \#* ]] && continue

    found=false
    for supported in "${SUPPORTED_LANGUAGES[@]}"; do
        if [ "$supported" = "$lang" ]; then
            found=true
            break
        fi
    done
    if [ "$found" = false ]; then
        echo "Warning: Skipping unsupported language: $lang"
        continue
    fi

    if [ "$SCHEDULED" = true ]; then
        tz=$(get_timezone_for_language "$lang")
        delay=$(calculate_delay "$tz")

        if [ "$delay" -eq 0 ]; then
            echo "Sending now [$lang] ($(TZ="$tz" date +%H:%M) $tz)"
            send_notification "$lang" "$title" "$body"
        else
            send_time=$(format_send_time "$tz" "$delay")
            echo "Scheduled [$lang] at $send_time (in $((delay / 60))m)"
            (sleep "$delay" && send_notification "$lang" "$title" "$body") &
            pids+=($!)
            scheduled_count=$((scheduled_count + 1))
        fi
    else
        send_notification "$lang" "$title" "$body"
    fi
done < "$TRANSLATIONS_FILE"

if [ "$SCHEDULED" = true ] && [ ${#pids[@]} -gt 0 ]; then
    echo ""
    echo "$scheduled_count notifications scheduled. Waiting for all to complete..."
    echo "(Press Ctrl+C to cancel pending notifications)"
    for pid in "${pids[@]}"; do
        wait "$pid" 2>/dev/null || true
    done
fi

echo ""
echo "Done."
