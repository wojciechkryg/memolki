#!/usr/bin/env bash
#
# Generate Play Store feature graphics for all flavors × locales.
#
# Usage:
#   ./scripts/screenshot/generate_feature_graphics.sh              # all flavors × all locales
#   ./scripts/screenshot/generate_feature_graphics.sh fruit_half    # one flavor × all locales
#   ./scripts/screenshot/generate_feature_graphics.sh fruit_half pl # one flavor × one locale

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

FLAVOR="${1:-all}"
LOCALE="${2:-}"

if [ "$FLAVOR" = "all" ]; then
    FLAVORS="fruit_half vegetable_half mammal_side bird_side"
else
    FLAVORS="$FLAVOR"
fi

for f in $FLAVORS; do
    if [ -n "$LOCALE" ]; then
        python3 "$SCRIPT_DIR/generate_feature_graphic.py" "$f" "$LOCALE"
    else
        python3 "$SCRIPT_DIR/generate_feature_graphic.py" "$f"
    fi
done
