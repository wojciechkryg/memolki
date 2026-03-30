# Video Recording for Google Play Store Ads

Automated recording of promo videos using `adb` screen recording, tap automation, and `ffmpeg` post-processing.

## Prerequisites

- **Emulator**: Pixel 2 (1080x1920, 9:16) — required for Google Play Store ads format
- **RECORDING_MODE**: `true` in `util/provider/RecordingModeProvider.kt`
- **App installed**: `./gradlew installFruitHalfDebug`
- **ffmpeg-full**: `brew install ffmpeg-full` (needed for drawtext/freetype support)
- Emulator running and connected via `adb`

## Usage

```bash
./scripts/record_video.sh [flavor] [locale]
```

Examples:
```bash
./scripts/record_video.sh                          # fruit_half, English
./scripts/record_video.sh fruit_half pl             # fruit_half, Polish
./scripts/record_video.sh vegetable_half de          # vegetable_half, German
./scripts/record_all_videos.sh                      # All 4 flavors × 32 languages (128 videos)
./scripts/record_all_videos.sh fruit_half           # All languages for one flavor
```

Flavors: `fruit_half`, `vegetable_half`, `mammal_side`, `bird_side`

Output: `~/Desktop/memolki_recordings/{flavor}/{locale}.mp4`

## Video flow (~30s at 1.5x speed)

The script navigates to the game board before recording starts, so the video opens directly on gameplay.

| Act | What happens |
|-----|-------------|
| 1. Struggle on 3x4 | Mismatches — try cards, fail |
| 2. Solve 3x4 | Rapid matching of all 6 pairs (mixed order) |
| 3. End game | Coin reward with total coins shown |
| 4. Collection | Tap "Unlock New Card" → unlock a card with coins |
| 5. Navigate to 5x6 | Back → Menu → New Game → 5x6 |
| 6. Struggle on 5x6 | Random-looking attempts, keep failing |
| 7. End card | Blur fades in + localized "Can you do better?" text |

## What the script does

1. **Enables DND + demo mode** — no notifications, clean status bar (12:00, full battery, wifi)
2. **Clears app data** — fresh state with 20 unlocked cards and 473 coins (via `PrepareRecordingCoinsUseCase`)
3. **Sets per-app locale** via `adb shell cmd locale set-app-locales` (Android 13+ API)
4. **Navigates to 3x4** board before recording starts
5. **Records** via `adb shell screenrecord` (video only, no audio)
6. **Performs taps** via `adb shell input swipe` at exact coordinates
7. **Post-processes** with ffmpeg: 1.5x speedup + background music + blur/text ending
8. **Cleans up** — disables demo mode, removes raw files

## RECORDING_MODE

`RECORDING_MODE` is a compile-time constant in `util/provider/RecordingModeProvider.kt`. When `true`, it affects:

| File | Effect |
|------|--------|
| `RecordingModeProvider.kt` | Single source of truth for the flag (`util/provider/`) |
| `ClickIndicatorOverlay.kt` | Shows cursor overlay on taps, blocks rapid multi-taps |
| `AppModule.kt` | Fresh `Random(0)` per injection (not singleton) for deterministic card order |
| `AppActivity.kt` | Wraps entire app in `ForceLtr` for consistent tap coordinates in RTL locales |
| `PrepareRecordingCoinsUseCase.kt` | Seeds 473 coins on first launch |
| `UnlockedCardPairsLocalDataSource.kt` | Starts with 20 unlocked cards (instead of 5) |
| `EndGameViewModel.kt` | Hides Watch Ad, Free Coins, Share; always shows Unlock New Card; skips notification request |
| `EndGameContent.kt` | Shows coins display, filters monetization menu items |
| `CollectionViewModel.kt` | Hides Watch Ad unlock (replaces with locked slot to preserve total count) |
| `MenuContent.kt` | Hides "more apps" section |
| `ForceLtr.kt` | Helper composable used for click overlay and level text in RTL locales |

**CI guard**: `pull_request.yml` checks for `RECORDING_MODE = true` and fails the build — must be set to `false` before merging.

## Post-processing

The script uses `ffmpeg-full` (`/opt/homebrew/opt/ffmpeg-full/bin/ffmpeg`) to:

1. **Speed up 1.5x** — `setpts=PTS/1.5` for video
2. **Add background music** — `app/src/main/res/raw/music_background.ogg` at normal speed, fades out at the end
3. **Blur ending** — `boxblur=8:8` fades in over 0.4s during the last 3 seconds
4. **Localized text** — "Can you do better?" in Patrick Hand font (`app/src/main/res/font/patrickhand_regular.ttf`), fades in 0.5s after blur starts, translated for each of the 32 locales

## Tap coordinates (Pixel 2, 1080x1920)

All coordinates mapped via `adb shell uiautomator dump`.

### 3x4 game grid (with 20 unlocked cards, fresh Random(0))

```text
┌────────────┬────────────┬────────────┐
│ plum half  │ apple whole│ plum whole │
│ (201,452)  │ (540,452)  │ (879,452)  │
├────────────┼────────────┼────────────┤
│apple whole │ raspberry  │raspberry ½ │
│ (201,791)  │ (540,791)  │ (879,791)  │
├────────────┼────────────┼────────────┤
│kiwi whole  │ apple half │ cherry half│
│ (201,1130) │ (540,1130) │ (879,1130) │
├────────────┼────────────┼────────────┤
│cherry whole│ peach whole│ kiwi half  │
│ (201,1469) │ (540,1469) │ (879,1469) │
└────────────┴────────────┴────────────┘
```

### Navigation

| Element | Tap center |
|---------|-----------|
| Menu → New Game | 540, 945 |
| Choose Level → 3x4 | 540, 642 |
| Choose Level → 5x6 | 540, 1574 |
| End game → Unlock New Card | 541, 1297 |
| Collection → coin-unlock card | 283, 938 |

## Timing variables

| Variable | Default | Purpose |
|----------|---------|---------|
| `TAP_DURATION` | 400ms | Hold duration for each tap |
| `DELAY_OVERLAY` | 1.0s | Click overlay animation |
| `DELAY_MATCH` | 1.8s | Match animation settle |
| `DELAY_MISMATCH` | 2.5s | Cards flip back after mismatch |
| `DELAY_TRANSITION` | 0.7s | Screen transition |
| `DELAY_ENDGAME` | 3.0s | End game screen appear |
| `DELAY_5X6_CARD` | 1.2s | Between card taps on 5x6 |
| `SPEED` | 1.5 | Video speedup factor |
| `BLUR_DURATION` | 3s | End card blur duration |

## Multi-flavor support

Card grid positions are identical across all flavors — fresh `Random(0)` per injection produces the same shuffle order. The script resolves the package name from the flavor argument.

```bash
./gradlew installFruitHalfDebug installVegetableHalfDebug installMammalSideDebug installBirdSideDebug
```

## Troubleshooting

- **Taps landing in wrong places**: Verify you're on the Pixel 2 emulator (1080x1920). Run `adb shell wm size` to check. Use `adb shell uiautomator dump` to remap coordinates.
- **Cards not flipping**: Increase `DELAY_OVERLAY` or `DELAY_5X6_CARD` — the click overlay blocks taps while animating.
- **Mismatch cards not flipping back**: Increase `DELAY_MISMATCH`.
- **Language not changing**: The app uses per-app locale (`LocaleManager` on API 33+). The script uses `adb shell cmd locale set-app-locales`. System locale commands won't work.
- **Level text reversed in RTL**: Fixed via `ForceLtr` composable. If other text reverses, wrap it similarly.
- **ffmpeg drawtext not found**: Install `ffmpeg-full` (`brew install ffmpeg-full`), not the regular `ffmpeg`.
- **Tests failing**: Expected when `RECORDING_MODE = true` — the test suite expects production defaults (5 cards, 0 coins).
