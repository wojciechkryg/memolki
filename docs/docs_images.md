# 🖼 images

## Generating card images

Use the assistant script to generate and resize images:

```bash
# All cards for a flavor
python3 scripts/image_generation/assist.py list/mammal_side.txt --variants side,front

# Specific cards only
python3 scripts/image_generation/assist.py list/fruit_half.txt --variants whole,half --cards "Banana,Apple"

# Skip already generated cards (resume after quit)
python3 scripts/image_generation/assist.py list/mammal_side.txt --variants side,front --skip-existing
```

The script:
1. Shows the next card name and copies it to clipboard
2. You paste it into Gemini web UI (with the Gem prompt), generate, and download
3. Script detects the new file in `~/Downloads`, renames and resizes it, then deletes the download

## Image specs

- Format: JPEG, square
- Quality: 50
- Densities:
    - drawable-xxxhdpi: 1024x1024
    - drawable-xxhdpi: 720x720
    - drawable-xhdpi: 512x512
    - drawable-hdpi: 384x384
    - drawable-mdpi: 256x256
- Naming: `img_{snake_case_name}_{variant}.jpg`
- Location: `androidApp/src/{flavor}/res/drawable-{density}/`

## Card lists

Card names are defined in `list/` at the project root.

## Prompt

The generation prompt is stored in `scripts/image_generation/prompt.txt` and saved as a Gem in Gemini for quick access.
