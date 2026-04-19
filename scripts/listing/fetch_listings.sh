#!/bin/bash

# Fetches current Play Store listings (title, short description, full description)
# for all flavors and saves them to androidApp/src/{flavor}/play/listings/{locale}/
#
# Prerequisites:
# - Set PLAY_SERVICE_ACCOUNT_PATH in secrets.properties
# - Run: ./scripts/listing/fetch_listings.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

FLAVORS=("fruitHalf" "vegetableHalf" "mammalSide" "birdSide")

echo "Fetching Play Store listings for all flavors..."

for flavor in "${FLAVORS[@]}"; do
    capitalized="$(echo "${flavor:0:1}" | tr '[:lower:]' '[:upper:]')${flavor:1}"
    echo ""
    echo "=== Bootstrapping $flavor ==="
    "$PROJECT_ROOT/gradlew" -p "$PROJECT_ROOT" "bootstrap${capitalized}ReleaseListing" --no-configuration-cache || echo "Warning: $flavor bootstrap had non-fatal errors (in-app products API), listings were still fetched."
done

echo ""
echo "Done! Listings saved to androidApp/src/{flavor}/play/listings/{locale}/"
echo ""
echo "Directory structure per flavor:"
echo "  title.txt             - App title"
echo "  short-description.txt - Short description"
echo "  full-description.txt  - Full description"
