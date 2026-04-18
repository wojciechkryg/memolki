# Play Store Screenshots & Feature Graphics

Automated generation of Play Store listing assets using ADB screen capture and Python/Pillow compositing.

## Prerequisites

- **Emulator**: Pixel 9 Pro (1280×2856) running and connected via `adb`
- **RECORDING_MODE**: `true` in `util/provider/RecordingModeProvider.kt` **before** running the orchestrator. The script rebuilds and installs the APK — if `RECORDING_MODE = false`, the installed app won't seed boards, and taps will miss cards on shots 1, 3, 4. The orchestrator now has a preflight check that fails fast if the flag is not `true`.
- **Python 3 + Pillow**: `pip install Pillow`

> ⚠️ After flipping `RECORDING_MODE`, always re-run `generate_all_screenshots.sh` (which reinstalls the APK) — a stale install ignores the flag change.

## Quick Start

```bash
# All screenshots for default flavors (fruit_half, mammal_side, bird_side) × 32 locales
./scripts/screenshot/generate_all_screenshots.sh

# Single flavor × all locales
./scripts/screenshot/generate_all_screenshots.sh fruit_half

# Specific flavors + locales
./scripts/screenshot/generate_all_screenshots.sh "fruit_half bird_side" en,pl

# Feature graphics only (no emulator needed)
./scripts/screenshot/generate_feature_graphics.sh
```

`generate_all_screenshots.sh` automatically builds and installs each flavor via Gradle before processing locales. No manual `./gradlew install*` needed.

### Manual per-locale (for debugging)

```bash
./scripts/screenshot/take_screenshots.sh fruit_half en
python3 scripts/screenshot/compose_screenshots.py fruit_half en ~/Desktop/memolki_screenshots/fruit_half/en
```

## Screenshots (5 per locale)

| # | Screen | Text (EN) |
|---|--------|-----------|
| 1 | 3×4 Gameplay (2 matched + 1 revealed) | "not your usual memory game!" |
| 2 | Collection (unlocked, scrolled to top) | "how many can you unlock?" |
| 3 | 5×6 Gameplay (12 of 15 pairs matched) | "think you can handle this?" |
| 4 | Daily Challenge End (3 stars) | "ready for today's puzzle?" |
| 5 | Collection (locked, scrolled to bottom) | "collect them all!" |

Bottom-bleed layout: device anchored at bottom with ~15% bleeding off the canvas edge (Niagara-style hero shot). Text sits in the top ~20% of the card. 7° rotation, anti-aliased frames, per-screenshot layering with neighbors peeking from left/right. All overlay texts are lowercase to match brand identity.

## Feature Graphics (1024×500)

```bash
python3 scripts/screenshot/generate_feature_graphic.py fruit_half       # all 32 locales
python3 scripts/screenshot/generate_feature_graphic.py fruit_half pl    # single locale
./scripts/screenshot/generate_feature_graphics.sh                       # all 4 flavors × 32 locales
```

Layout: flavor background, `ic_logo_{flavor}.png` on left, localized chip labels on right. Font auto-sizes for long translations.

## Output Paths

```
app/src/{flavor}/play/listings/{locale}/graphics/phone-screenshots/{1..5}.jpg
app/src/{flavor}/play/listings/{locale}/graphics/feature-graphic/1.png
```

## Scripts

| Script | Purpose |
|--------|---------|
| `generate_all_screenshots.sh` | Orchestrator — builds + installs flavors, runs take + compose for all locales |
| `take_screenshots.sh` | ADB automation — captures 5 raw PNGs from emulator |
| `compose_screenshots.py` | Pillow — device frame + text overlay → final JPEGs |
| `generate_feature_graphic.py` | Pillow — logo + localized chips → feature graphic PNG |
| `generate_feature_graphics.sh` | Bash wrapper — runs feature graphic for all flavors/locales |

## Flavor Colors

| Flavor | Background |
|--------|-----------|
| fruit_half | `#FFEAA1` (yellow) |
| vegetable_half | `#E6A0A0` (rose) |
| mammal_side | `#E2BA8B` (tan) |
| bird_side | `#B1DBE7` (blue) |

## Default Flavors

`generate_all_screenshots.sh` defaults to `fruit_half mammal_side bird_side`. `vegetable_half` is excluded from defaults but supported if passed explicitly.

## Localization

- **32 locales**: en, ar, cs, da, de, el, es, et, fi, fr, hi, hu, in, it, iw, ja, ko, lt, lv, nl, no, pl, pt, ro, ru, sk, sl, sv, tr, uk, vi, zh
- **Screenshot text**: `compose_screenshots.py` → `get_texts()`
- **Feature graphic chips**: `generate_feature_graphic.py` → `get_chips()`
- **Font**: Patrick Hand (default), Arial Unicode (fallback for ar, el, hi, iw, ja, ko, ru, uk, zh)

## Card Layouts

Both regular board (`Random(0)`) and daily challenge (seed fixed to `0` in RECORDING_MODE) are fully deterministic — card positions are stable across dates and don't need re-probing.

Tap coordinates are for Pixel 9 Pro (1280×2856, immersive mode). Remap with `adb shell uiautomator dump` if switching emulator.
