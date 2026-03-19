# 👆 Click Indicator Overlay

A temporary visual overlay that shows a cursor image at each tap position during screen recordings
and demo videos. Not intended for production use.

## Location

`app/src/main/java/com/wojdor/memolki/ui/component/ClickIndicatorOverlay.kt`

## How it works

The overlay wraps the entire app content in `AppActivity.kt`. When enabled, it intercepts all touch
events and renders a cursor drawable (`res/drawable/ic_cursor.png`) at each tap point with a
fade-in → hold → fade-out animation.

## Enable / Disable

Toggle the constant at the top of the file:

```kotlin
private const val SHOW_CLICK_INDICATOR = false  // false = disabled (production default)
private const val SHOW_CLICK_INDICATOR = true   // true = enabled (recording mode)
```

When disabled, the composable is a no-op pass-through — zero overhead.

## CI guard

The PR workflow (`.github/workflows/pull_request.yml`) includes a `debug-flags-check` job that fails
if `SHOW_CLICK_INDICATOR = true` is detected. This prevents accidentally shipping the overlay
enabled.
