#!/bin/bash

# Updates Play Store listings (title, short description, full description)
# for all flavors from local files at androidApp/src/{flavor}/play/listings/{locale}/
#
# Usage:
#   ./scripts/listing/update_listings.sh              # Update all flavors
#   ./scripts/listing/update_listings.sh fruitHalf     # Update specific flavor
#
# Prerequisites:
# - Set PLAY_SERVICE_ACCOUNT_PATH in secrets.properties
# - First run fetch_listings.sh to get the current structure
# - Edit the files in androidApp/src/{flavor}/play/listings/{locale}/
# - Then run this script to push changes

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

ALL_FLAVORS=("fruitHalf" "vegetableHalf" "mammalSide" "birdSide")

if [ $# -gt 0 ]; then
    FLAVORS=("$@")
else
    FLAVORS=("${ALL_FLAVORS[@]}")
fi

echo "Updating Play Store listings..."

for flavor in "${FLAVORS[@]}"; do
    capitalized="$(echo "${flavor:0:1}" | tr '[:lower:]' '[:upper:]')${flavor:1}"
    listing_dir="$PROJECT_ROOT/androidApp/src/$flavor/play/listings"

    if [ ! -d "$listing_dir" ]; then
        echo "Warning: No listings found for $flavor at $listing_dir"
        echo "  Run fetch_listings.sh first to bootstrap the directory structure."
        continue
    fi

    echo ""
    echo "=== Publishing $flavor listings ==="
    "$PROJECT_ROOT/gradlew" -p "$PROJECT_ROOT" "publish${capitalized}ReleaseListing" --no-configuration-cache
done

echo ""
echo "Done! Listings updated on Google Play."
