#!/usr/bin/env python3
"""Semi-automated image generation assistant for Memolki.

Workflow:
1. Script shows you what to generate next and copies it to clipboard
2. You paste the item name in Gemini web UI (with your Gem), generate, and download
3. Script detects the new download, renames and resizes it automatically
4. Press Enter for the next card

Usage:
    python3 scripts/image_generation/assist.py list/test_instruments.txt
    python3 scripts/image_generation/assist.py list/mammal_side.txt --variants side,front
"""

import argparse
import re
import subprocess
import sys
import time
import unicodedata
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    print("Error: Pillow is required. Install it with: pip3 install Pillow")
    sys.exit(1)

# --- Constants ---

SCRIPT_DIR = Path(__file__).parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
IMAGE_EXTENSIONS = ("*.png", "*.jpg", "*.jpeg", "*.webp")
JPEG_QUALITY = 50

FLAVOR_DIR = {
    "mammal_side": "mammalSide",
    "bird_side": "birdSide",
    "vegetable_half": "vegetableHalf",
    "fruit_half": "fruitHalf",
}

DENSITIES = {
    "drawable-xxxhdpi": 1024,
    "drawable-xxhdpi": 720,
    "drawable-xhdpi": 512,
    "drawable-hdpi": 384,
    "drawable-mdpi": 256,
}

# --- Helpers ---


def to_snake_case(name):
    """Convert readable name to snake_case filename (e.g. 'Açaí' -> 'acai')."""
    normalized = unicodedata.normalize("NFKD", name)
    ascii_name = normalized.encode("ascii", "ignore").decode("ascii")
    return re.sub(r"[^a-z0-9]+", "_", ascii_name.lower()).strip("_")


def copy_to_clipboard(text):
    subprocess.Popen(["pbcopy"], stdin=subprocess.PIPE).communicate(text.encode("utf-8"))


def make_filename(card_name, variant):
    snake = to_snake_case(card_name)
    return f"img_{snake}_{variant}.jpg" if variant else f"img_{snake}.jpg"


def format_display(card_name, variant):
    return f"{card_name} ({variant})" if variant else card_name


# --- File I/O ---


def load_card_list(list_path):
    """Load card names from list file (one per line, # comments and blank lines ignored)."""
    return [
        line.strip()
        for line in list_path.read_text().splitlines()
        if line.strip() and not line.strip().startswith("#")
    ]


# --- Download detection ---


def get_latest_image(downloads_dir):
    """Find the most recently modified image file in downloads directory."""
    latest, latest_mtime = None, 0
    for ext in IMAGE_EXTENSIONS:
        for f in downloads_dir.glob(ext):
            mtime = f.stat().st_mtime
            if mtime > latest_mtime:
                latest_mtime = mtime
                latest = f
    return latest, latest_mtime


def wait_for_new_download(downloads_dir, before_mtime):
    """Block until a new image appears in downloads directory."""
    print("    Waiting for download...", end="", flush=True)
    dots = 0
    while True:
        latest, mtime = get_latest_image(downloads_dir)
        if latest and mtime > before_mtime:
            time.sleep(0.5)  # let the file finish writing
            print(f" got it: {latest.name}")
            return latest
        time.sleep(1)
        dots += 1
        if dots % 5 == 0:
            print(".", end="", flush=True)


# --- Image processing ---


def save_resized(image, flavor_dir, card_name, variant, output_dir):
    """Resize image to all Android densities and save as JPEG."""
    if image.size != (1024, 1024):
        image = image.resize((1024, 1024), Image.LANCZOS)
    if image.mode != "RGB":
        image = image.convert("RGB")

    filename = make_filename(card_name, variant)

    for density_folder, size in DENSITIES.items():
        dest_dir = (output_dir or PROJECT_ROOT / "app" / "src" / flavor_dir / "res") / density_folder
        dest_dir.mkdir(parents=True, exist_ok=True)
        image.resize((size, size), Image.LANCZOS).save(dest_dir / filename, "JPEG", quality=JPEG_QUALITY)

    print(f"    Saved {filename} at all densities")


# --- Queue building ---


def build_queue(selected_cards, selected_variants, flavor_dir, output_dir, skip_existing):
    queue = []
    for card in selected_cards:
        for variant in selected_variants:
            filename = make_filename(card, variant)

            if skip_existing:
                check_dir = (output_dir or PROJECT_ROOT / "app" / "src" / flavor_dir / "res") / "drawable-xxxhdpi"
                if (check_dir / filename).exists():
                    continue

            queue.append((card, variant, filename))
    return queue


# --- Main ---


def parse_args():
    parser = argparse.ArgumentParser(description="Semi-automated image generation assistant")
    parser.add_argument("list_file", help="Path to card list file (e.g. list/mammal_side.txt)")
    parser.add_argument("--variants", help="Comma-separated variants (e.g. side,front or whole,half)")
    parser.add_argument("--cards", help="Comma-separated card names to generate (default: all)")
    parser.add_argument("--output-dir", help="Override output directory")
    parser.add_argument("--downloads-dir", default=str(Path.home() / "Downloads"), help="Downloads directory to watch (default: ~/Downloads)")
    parser.add_argument("--skip-existing", action="store_true", help="Skip cards that already have images")
    return parser.parse_args()


def print_header(list_path, flavor_dir, queue_size, downloads_dir):
    print(f"=== Memolki Image Generation Assistant ===")
    print(f"List: {list_path.name} -> {flavor_dir}/")
    print(f"Cards to generate: {queue_size}")
    print(f"Watching: {downloads_dir}")
    print()
    print("Workflow:")
    print("  1. Paste the item name in Gemini (auto-copied to clipboard)")
    print("  2. Download the generated image")
    print("  3. Script auto-detects, renames, and resizes")
    print()
    print("Controls: Enter = proceed, s = skip, q = quit")
    print()


def process_card(card, variant, flavor_dir, output_dir, downloads_dir):
    _, before_mtime = get_latest_image(downloads_dir)

    user_input = input("    Download it, then press Enter (s=skip, q=quit): ").strip().lower()
    if user_input == "q":
        return "quit"
    if user_input == "s":
        print("    Skipped.")
        return "skipped"

    downloaded = wait_for_new_download(downloads_dir, before_mtime)

    try:
        image = Image.open(downloaded)
        save_resized(image, flavor_dir, card, variant, output_dir)
        downloaded.unlink()
        print("    Done!")
    except Exception as e:
        print(f"    Error processing {downloaded.name}: {e}")

    return "ok"


def main():
    args = parse_args()

    list_path = Path(args.list_file)
    if not list_path.exists():
        print(f"Error: List file not found: {list_path}")
        sys.exit(1)

    downloads_dir = Path(args.downloads_dir)
    list_name = list_path.stem
    flavor_dir = FLAVOR_DIR.get(list_name, list_name)
    output_dir = Path(args.output_dir) if args.output_dir else None

    all_cards = load_card_list(list_path)
    selected_cards = [c.strip() for c in args.cards.split(",")] if args.cards else all_cards
    selected_variants = [v.strip() for v in args.variants.split(",")] if args.variants else [None]

    queue = build_queue(selected_cards, selected_variants, flavor_dir, output_dir, args.skip_existing)

    if not queue:
        print("Nothing to generate! All images already exist.")
        return

    print_header(list_path, flavor_dir, len(queue), downloads_dir)

    succeeded = 0
    for i, (card, variant, filename) in enumerate(queue):
        display = format_display(card, variant)
        copy_to_clipboard(display)
        print(f"--- [{i + 1}/{len(queue)}] Generate: {display} (copied to clipboard) ---")

        result = process_card(card, variant, flavor_dir, output_dir, downloads_dir)
        if result == "quit":
            print("\nQuitting.")
            return
        if result == "ok":
            succeeded += 1

        print()

    print(f"\n=== Complete! {succeeded}/{len(queue)} images generated ===")


if __name__ == "__main__":
    main()
