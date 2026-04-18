# AGENTS.md

This file provides guidance to AI coding agents when working with code in this repository.

## General Guidance

- **When unsure, ask.** If a request is ambiguous, ask the user to clarify rather than guessing and doing extra work.
- **Scripts exist — run them directly.** Don't read/analyze content for scripted operations — just execute the script. If the user wants content changes, they'll say so explicitly.

### Common operations quick reference

| User says | Run |
|-----------|-----|
| "update listings" | `./scripts/listing/update_listings.sh` |
| "fetch listings" | `./scripts/listing/fetch_listings.sh` |
| "generate screenshots" | `./scripts/screenshot/generate_all_screenshots.sh` |
| "generate feature graphics" | `./scripts/screenshot/generate_feature_graphics.sh` |
| "record video" | `./scripts/recording/record_video.sh {flavor} {locale}` |
| "record all videos" | `./scripts/recording/record_all_videos.sh` |
| "upload videos" | `./scripts/youtube/upload_videos.sh` |
| "write video urls" | `./scripts/youtube/write_video_urls.sh` |
| "add ads video assets" | `./scripts/youtube/add_ads_video_assets.sh` |
| "run video pipeline" | `./scripts/youtube/run_pipeline.sh` |
| "video setup" | `./scripts/youtube/setup.sh` |
| "send notification" | `./scripts/notifications/send_push_notification.sh {file}` |

## Project Overview

Memolki is an Android card-matching memory game built with Jetpack Compose. It ships as multiple app flavors (each with a unique theme, package name, and billing key). Check `app/build.gradle.kts` for the current list of flavors under `productFlavors`.

## Documentation

Detailed docs live in `docs/`. Consult the relevant doc before working in that area:

| Doc | Topic |
|-----|-------|
| [`docs/docs_logo.md`](docs/docs_logo.md) | Logo generation and specifications |
| [`docs/docs_images.md`](docs/docs_images.md) | Card image generation, resizing, and density buckets |
| [`docs/docs_icons.md`](docs/docs_icons.md) | App icon setup and guidelines |
| [`docs/docs_new_app_flavor_setup.md`](docs/docs_new_app_flavor_setup.md) | Full checklist for adding a new WLA flavor |
| [`docs/docs_click_indicator.md`](docs/docs_click_indicator.md) | Click indicator overlay for recording mode |
| [`docs/docs_listing.md`](docs/docs_listing.md) | Play Store listing management (fetch/update scripts, character limits) |
| [`docs/docs_recording.md`](docs/docs_recording.md) | Video recording for Play Store ads |
| [`docs/docs_youtube.md`](docs/docs_youtube.md) | YouTube video upload and Play Store linking |

## Build & Test Commands

The **default flavor** is `fruitHalf` — use it for builds, tests, and installs unless told otherwise.

After making changes, always install and launch the app on the connected device/emulator to verify the build works at runtime (not just compilation).

```bash
# Build debug APK
./gradlew assembleFruitHalfDebug

# Install debug APK on connected device/emulator and launch it
./gradlew installFruitHalfDebug && adb shell am start -n com.wojdor.memolki.fruithalf/com.wojdor.memolki.ui.app.AppActivity

# Run all unit tests — you MUST specify a flavor, the unqualified task name is ambiguous
./gradlew testFruitHalfDebugUnitTest

# Run a single test class
./gradlew testFruitHalfDebugUnitTest --tests "com.wojdor.memolki.ui.feature.menu.MenuViewModelTest"

# Run a single test method
./gradlew testFruitHalfDebugUnitTest --tests "com.wojdor.memolki.ui.feature.menu.MenuViewModelTest.when initial load is done then the state is updated with menu"

# Verify screenshot tests — run after building/installing to catch visual regressions
./gradlew verifyPaparazziFruitHalfDebug

# Record new screenshot references — run after intentional UI changes
./gradlew recordPaparazziFruitHalfDebug

# Generate unit test coverage report (excludes screenshot tests)
./gradlew koverXmlReportFruitHalfDebug -PcoverageTestExclude='com.wojdor.memolki.screenshot.*'

# Generate screenshot test coverage report
./gradlew clean koverXmlReportFruitHalfDebug -PcoverageTestFilter='com.wojdor.memolki.screenshot.*'
```

After making UI changes, always run `verifyPaparazziFruitHalfDebug` alongside the build/install step. If screenshots differ intentionally, run `recordPaparazziFruitHalfDebug` to update the references and commit the updated PNGs.

## Setup

A `secrets.properties` file is needed in the root directory with:
- `<FLAVOR_UPPER_SNAKE>_BILLING_KEY` entry for each flavor (see `app/build.gradle.kts`)
- `FIREBASE_PROJECT_ID` — required by `scripts/notifications/send_push_notification.sh`

Adding a new flavor has a checklist in `docs/docs_new_app_flavor_setup.md`.

## Architecture

**MVI + Clean Architecture** with three layers:

```
UI (Compose) → Domain (Use Cases) → Data (Repositories → DataSources)
```

**Layer boundaries are strict:** Use cases depend only on repositories (and other use cases) — never import data-layer internals (`data/local/database/` entities, `data/mapper/`). Repositories are responsible for mapping between data entities and domain models.

### UseCase Layer

Two base classes in `domain/usecase/base/`:
- `BaseUseCase<R>` — no-parameter use case, invoked as `useCase()`
- `BaseParameterUseCase<P, R>` — parameterized, invoked as `useCase(param)`

Both return `Flow<Result<R>>` via `operator fun invoke()`. Override `execute()` to implement.

Conventions:
- Named `[Verb][Noun]UseCase` (e.g. `GetSettingsUseCase`, `ToggleSettingsUseCase`)
- Constructor-injected with `@Inject`
- **Dispatcher is always the 1st constructor parameter**
- Dispatcher choice: `@IoDispatcher` for anything touching system services, repositories, or I/O; `@DefaultDispatcher` for pure in-memory computation; `@MainDispatcher` only when the Android API requires the main thread
- **Do NOT use `runCatching`** — the base class `.catch` block handles exceptions and reports to Crashlytics (see Analytics & Crashlytics section)

Reference examples:
- Simple: `domain/usecase/GetSettingsUseCase.kt`
- With parameter: `domain/usecase/ToggleSettingsUseCase.kt`

### Domain Models

Sealed classes with `@Parcelize`, often including:
- Abstract properties in the sealed parent
- Data class subclasses for concrete types
- `object Empty` sentinel for default/initial values
- Resource annotations (`@field:StringRes`, `@field:DrawableRes`) for UI resources

Reference: `domain/model/CardModel.kt`, `domain/model/SettingModel.kt`

### Data Layer

- **Persistence:** Two storage mechanisms:
  - **Encrypted DataStore Preferences** (`data/local/datastore/`) — for key-value settings and user data (coins, streaks, unlocked cards). Uses `data/crypto/Encryptor` for sensitive values. Never use SharedPreferences.
  - **Room Database** (`data/local/database/`) — for structured, growing data (e.g. daily challenge results). `AppDatabase` in `data/local/database/`, entities and DAOs in subdirectories. Provided via Hilt in `DataModule`. Database version tracked as `private const val DATABASE_VERSION` in `AppDatabase.kt`. When bumping `DATABASE_VERSION`, add a proper migration to preserve user data.
- **Entities:** DataStore entities in `data/entity/`, mapped via `data/mapper/`. Room entities live next to their DAOs in `data/local/database/`.
- **Repositories:** in `data/repository/`, orchestrate data sources from `data/local/`. Repositories map between data entities and domain models — never expose Room entities or DataStore keys in the public API
- **Backup & Restore:** `res/xml/backup_rules.xml` (API < 31) and `res/xml/data_extraction_rules.xml` (API 31+) include the `sharedpref` and `database` domains, plus the `file` domain with `path="datastore/"`. When adding a new persistence mechanism, update both files

### MVI Pattern

Each feature screen follows this structure under `ui/feature/{name}/`:

| File | Purpose |
|---|---|
| `{Name}State.kt` | `@Parcelize` data class implementing `UiState`, all properties have defaults |
| `{Name}Intent.kt` | Sealed class implementing `UiIntent`, entries named `On[Action]` |
| `{Name}Effect.kt` | Sealed class implementing `UiEffect` for one-shot side effects (navigation, toasts, showing overlays) |
| `{Name}ViewModel.kt` | `@HiltViewModel` extending `MviViewModel<Intent, State>` |
| `{Name}Screen.kt` | `@Composable` with three-level hierarchy (see below) |
| `{Name}Callbacks.kt` | (optional) Data class grouping lambdas for the screen, defaults to `= {}` |

**Base class:** `MviViewModel` (`ui/base/MviViewModel.kt`) manages intent→state flow via `sendIntent()`, `onIntent()`, `sendState { copy(...) }`, `sendEffect()`. State is persisted through `SavedStateHandle`.

ViewModel conventions:
- Load initial data in `init {}` block
- Handle intents in `onIntent()` with `when` expression — delegate to small private functions
- Call use cases as `useCase().onEach { ... }.launchIn(viewModelScope)`. For fire-and-forget: `useCase(param).launchIn(viewModelScope)` (no empty `onEach {}`)
- When combining multiple use cases, create a new UseCase that uses `combine()` internally (see `CanUnlockNewCardUseCase`, `GetLanguagesWithCurrentUseCase`) — don't `combine` in ViewModels
- Handle results with `.onSuccess { }` / `.onFailure { }` — never use `getOrNull()`, `getOrThrow()`, or `first()` on use case flows (use case flows may emit multiple values over time, and `first()` silently drops updates; `getOrNull()` discards error information; `getOrThrow()` crashes on failure)
- **No `delay()` for UI timing** — if the ViewModel needs to wait for an animation, let the UI send an intent when the animation completes instead
- **No Android framework calls** in ViewModels or UseCases (e.g. `AppCompatDelegate`, `Context`, `LocaleManager`) — wrap them in Provider classes under `util/provider/`. Analytics is the exception — use `util/analytics/Analytics` directly
- **No redundant state** — don't create separate `var` flags when a state field already covers the same purpose
- **Strict MVI communication** — View ↔ ViewModel communication only through State, Effect, and Intent. Never expose public properties or functions on ViewModels beyond `sendIntent()` and what `MviViewModel` provides
- **Effects vs State** — Effects are for one-time events that the UI cannot derive from State (navigation, toasts, launching external intents). State is for anything that drives the UI (including flags like `isLanguageChangeInProgress` that control visibility, animations, or overlays). Never use an Effect to set local composable `remember` state — if the ViewModel knows about a condition, put it in State and let the UI observe it directly
- ViewModels survive activity recreation (config changes, locale changes)
- **All ViewModel state that affects behavior must go through `SavedStateHandle`** — private `var` fields reset on process death while `UiState` survives. If a flag drives branching logic (e.g. `isDailyChallenge`), it must be in State or SavedStateHandle, not a plain `var`

### Screen Composition (Three-Level Hierarchy)

Screens follow a strict composable layering — **always follow this exact structure**:

```kotlin
// 1. Public entry point — ONLY collects state and delegates
@Composable
fun {Name}Screen(
    viewModel: {Name}ViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

// 2. Private — handles one-shot side effects (navigation, toasts, animations)
@Composable
private fun HandleEffect(
    viewModel: {Name}ViewModel,
    navController: NavController,
) {
    CollectUiEffects(viewModel) { effect ->
        when (effect) { ... }
    }
}

// 3. Private — creates Callbacks wiring lambdas to sendIntent(), calls stateless screen
@Composable
private fun HandleState(
    viewModel: {Name}ViewModel,
    state: {Name}State,
) {
    val callbacks = {Name}Callbacks(
        onSomething = { viewModel.sendIntent({Name}Intent.OnSomething) }
    )
    {Name}Screen(state, callbacks)
}

// 4. Private stateless — pure UI, previewable, takes only state + callbacks
@Composable
private fun {Name}Screen(
    state: {Name}State,
    callbacks: {Name}Callbacks = {Name}Callbacks()
) { ... }
```

Activity recreation notes (e.g. locale change triggers recreation):
- ViewModels survive recreation — `init` does NOT re-run, `SavedStateHandle` persists state
- `remember` resets on recreation, `rememberSaveable` persists — use `remember` to detect post-recreation state (e.g. `val wasChangingOnStart = remember { viewModel.uiState.value.someFlag }`)
- `sendEffect()` via Channel can be lost during recreation (old collector disposed before new one starts) — for critical post-recreation navigation, detect state in the composable instead

Key rules:
- The top-level function body is always exactly: collect state → `HandleEffect` → `HandleState`
- `HandleEffect` and `HandleState` are always `private @Composable` functions
- Effects are for **one-time events only** (see "Effects vs State" in the MVI Pattern section above). Never call domain/data layer APIs from Screen composables — that logic belongs in the ViewModel
- Input guards (e.g. preventing double-taps) belong in the ViewModel, not in callbacks
- Complex effect actions can be extracted into separate private functions (see `GameScreen.kt` → `openEndGameScreen()`)

Reference example (complete feature): `ui/feature/settings/` — has all 6 files including Callbacks.

### Compose Previews

Every screen and reusable component has `@Preview` functions. Conventions:
- `private fun {Name}Preview()` / `{Name}{Variant}Preview()` for multiple states (e.g. `SettingsScreenDisabledPreview`)
- Always wrapped in `AppTheme { ... }`
- Screen previews call the **stateless** composable (bottom of the three-level hierarchy), passing a hand-crafted state and default `Callbacks()`
- Component previews pass sample data directly
- Use `showBackground = true` on screen-level previews; optional on small components
- Use test drawables (`R.drawable.img_test_whole`, `R.drawable.img_test_half`) for image previews
- When preview state is reused across files, extract a `get{Name}StateForPreview()` function (see `CollectionScreen.kt`)

### Navigation

Jetpack Compose Navigation with nested graphs (flows) in `ui/app/AppNavigation.kt`:
- **Game flow:** ChooseBoard → Game → EndGame (ViewModels shared via flow scope)
- **Collection flow:** Collection → Shop → CardPairDetails
- **Settings flow:** Settings → ChangeLanguage

Shared ViewModels within a flow are scoped to the navigation graph's back stack entry (see `getGameViewModel()` pattern).

Routes with arguments use path segments: `"route/{argName}"`. Navigation uses `.replace("{argName}", value)`. See `Route.ENABLE_NOTIFICATIONS` and `Route.GAME` for the pattern. Deep link navigation is handled by `navigateFromDeepLink()` in `AppNavigation.kt`, which parses the URI and calls the appropriate `navigateTo*` function — preserving MENU on the back stack.

Navigation animations are centralized in `ui/app/NavAnimation.kt` (directional slides, consistent 500ms tween).

### Provider Pattern

Android framework calls (e.g. `Context`, `LocaleManager`, `PackageManager`) are abstracted into provider classes under `util/provider/`. This keeps UseCases and ViewModels free of Android dependencies and makes them testable.

Conventions:
- `open class` with `@Inject constructor` and `open` methods
- Context injected via `@param:ApplicationContext private val context: Context`

Testing:
- Each provider has a corresponding `Fake{Name}` class in `test/fake/` that extends it, passing `mockk()` as the context
- Fakes are **fully functional** — they hold state via private vars and override all methods with real behavior (no mocks, no no-ops)
- Fakes are bound in `TestModule` via `@Binds` (not `relaxedMockk()`)
- Tests `@Inject` the fake by its concrete type (e.g. `@Inject lateinit var fakeLocaleProvider: FakeLocaleProvider`) and use it to control state directly

Examples: `LocaleProvider` / `FakeLocaleProvider`, `AppInstalledProvider` / `FakeAppInstalledProvider`

### Dependency Injection

Hilt with KSP. Modules in `di/module/`:
- `AppModule.kt` — app-wide bindings (DataStore, encryption, providers)
- `DataModule.kt` — data sources, Room database (`AppDatabase`, DAOs), DataStore

Coroutine dispatchers are injected via qualifiers defined in `di/coroutine/`: `@DefaultDispatcher`, `@IoDispatcher`, `@MainDispatcher`. Never hardcode `Dispatchers.*` directly.

### UI Utilities

- **Responsive spacing:** `ui/theme/Dimensions.kt` — `spacingXS`/`S`/`M`/`L`/`XL` are composable properties that adapt based on `isTablet` (WindowSizeClass)
- **Click throttling:** `util/ClickUtils.kt` — `throttleClick()` composable wrapper prevents duplicate clicks (1s default)
- **Logging:** `util/extension/` — `Any.logD()` / `Any.logE()` use the class name as tag. `logE()` also reports to Firebase Crashlytics as a non-fatal exception

### Compose Style Conventions

- **Modifier chaining:** Each modifier on its own line when there are 2+ modifiers:
  ```kotlin
  modifier = Modifier
      .pulseEffect()
      .bounceClickEffect(),
  ```
- **Animated buttons:** When a button uses `pulseEffect()`, its text style must use `.animated()` (e.g. `MaterialTheme.typography.displaySmall.animated()`) so text scales with the pulse
- **Button with icon pattern:** Use `PaddingValues(top = spacingS, bottom = spacingS, start = spacingS, end = spacingL)` for buttons with a leading icon (see `ToggleSettingButton`, `CompareButton`)
- **Inline dp values:** Don't extract to a private val unless the value is reused — prefer `Modifier.size(64.dp)` over `private val ICON_SIZE = 64.dp`
- **Don't store transient UI data in the database:** Only persist data needed for retrieval. Transient display data (e.g. grid flip counts, challenge numbers) should flow through state/effects, not be stored in Room

### Reusable Components

Shared composables live in `ui/component/`. Before creating a new composable, check this directory for existing ones that fit. Extract here when a UI pattern is reused or complex enough to warrant isolation.

## Testing

- **Framework:** JUnit 4 + Turbine (Flow testing) + MockK
- **Base class:** `AppTest` (`test/AppTest.kt`) — sets up Dagger test component, `testDispatcher`, and MockK annotations
- **Test DI:** `TestModule` provides fakes and relaxed mocks for Android dependencies. `Random(0)` for deterministic randomness.
- **Test utilities:** `test/TestUtils.kt` — `relaxedMockk<T>()`, `verifyOnce()`, `coVerifyOnce()`
- **Test naming:** backtick-quoted descriptive names: `` `when X then Y` ``

Conventions:
- Extend `AppTest`, annotate with `@ExperimentalCoroutinesApi`
- `@Inject` real dependencies, `@RelaxedMockK` for mocked ones
- Override `inject(injector)` and construct SUT in `@Before setup()`
- Wrap tests in `runTest {}`, assert flows with Turbine's `.test { awaitItem() }`
- For one-shot flows (e.g. `flow { emit(...) }`), call `awaitComplete()` after consuming items to avoid "Unconsumed events" errors
- For long-lived flows (e.g. DataStore, StateFlow), no `awaitComplete()` needed
- Comments in tests are **only** `// given`, `// when`, `// then` — no other comments (no inline explanations, no `// region`/`// endregion`, no trailing descriptions)
- Use `assertTrue()`/`assertFalse()` — never `assertEquals(true, ...)` or `assertEquals(false, ...)`
- For static Android APIs (e.g. `AppCompatDelegate`), use `mockkStatic(...)` in `@Before`
- Add `fun inject(test: YourTestClass)` to `TestInjector` for each new test class
- **Fake singleton scoping:** `@Binds @Singleton` scopes the parent type (e.g. `LocaleProvider`), not the Fake itself. Injecting `FakeLocaleProvider` directly in a test creates a separate instance from the one Hilt gives to UseCases via `LocaleProvider`. When testing a UseCase that depends on other UseCases that use Fakes, construct the child UseCase manually with the injected Fake (see `GetLanguagesWithCurrentUseCaseTest`)

### Common mistakes

Before committing, re-read every changed file and watch for these:

- **Refactoring leftovers:** When moving or extracting code, check the source for now-unused constants, imports, and functions
- **Missing error paths:** If a ViewModel collects a `Result`, both success and failure must be handled
- **Test quality over quantity:** Test names must match what is asserted, assertions must check actual values (not just types), and each test must cover a distinct scenario
- **Follow existing patterns:** Before inventing a new approach, find how the same thing is already done elsewhere in the codebase and replicate it
- **Update snapshots:** After changing any drawable or UI component, run `recordPaparazzi{Flavor}Debug` to regenerate affected snapshots

### Coverage expectations

Every testable class must have a corresponding test class with **full branch coverage** — all code paths, conditions, and edge cases. When adding or modifying code, always add or update tests to cover every new/changed path.

**Must have tests:** UseCases, ViewModels, Repositories, DataSources, domain models, State/Intent/Effect data classes

Reference examples:
- UseCase test: `test/.../domain/usecase/GetSettingsUseCaseTest.kt`
- UseCase test (provider fake): `test/.../domain/usecase/GetCurrentLanguageTagUseCaseTest.kt`
- UseCase test (combining): `test/.../domain/usecase/GetLanguagesWithCurrentUseCaseTest.kt`
- ViewModel test: `test/.../ui/feature/menu/MenuViewModelTest.kt`

## Flavor Structure

Each flavor lives under `app/src/{flavorName}/` and contains:

```
app/src/{flavorName}/
├── java/com/wojdor/memolki/data/local/card/
│   └── AllCardPairsLocalDataSource.kt      # Card pair definitions for this flavor
├── res/
│   ├── drawable/
│   │   └── ic_logo.xml                     # Flavor-specific vector logo
│   ├── drawable-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/
│   │   └── img_*.jpg                       # Card images at each density
│   ├── mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/
│   │   └── ic_launcher*.png                # App icons
│   ├── values/
│   │   ├── colors.xml                      # References primary color from main
│   │   ├── strings.xml                     # Card names (translatable)
│   │   ├── strings_non_translatable.xml    # Google Play Games IDs
│   │   └── ad_mob_ids.xml                  # AdMob app/unit IDs
│   ├── values-{locale}/
│   │   └── strings.xml                     # Translated card names
│   └── xml/
│       └── shortcuts.xml                   # App shortcuts with flavor's applicationId
```

### Adding a New Flavor (code-side checklist)

Full checklist (including Google Play, AdMob, CI): `docs/docs_new_app_flavor_setup.md`

Code changes needed:
1. Add flavor to `flavorConfigs` in `app/build.gradle.kts`
2. Add package name to `<queries>` in `AndroidManifest.xml`
3. Add `object` entry in `domain/model/AppModel.kt` and include it in `all()`
4. Add `app_name_{flavor}` and `suffix_{flavor}` strings in `main/res/values/strings.xml`
5. Add `primary_{flavor}` color in `main/res/values/colors.xml`
6. Create the flavor source set directory with the structure above
7. Create `AllCardPairsLocalDataSource.kt` with card pair definitions (reference: `app/src/fruitHalf/`)
8. Create `res/xml/shortcuts.xml` with the flavor's `applicationId` in `targetPackage` (copy from existing flavor)
9. Add flavor to CI matrix in `.github/workflows/merge.yml`

### Card Images

Named `img_{name}_whole.jpg` and `img_{name}_half.jpg` (or `_side`/`_front` depending on flavor theme), provided at 5 density buckets (1024, 720, 512, 384, 256px square, JPEG quality 50).

Card names for each flavor are listed in `list/{flavor}.txt` (one per line, human-readable).

To generate and resize images use `scripts/image_generation/assist.py` — it walks through each card, copies the name to clipboard for pasting into Gemini, watches `~/Downloads` for the result, and auto-resizes to all densities. See `docs/docs_images.md` for details.

## Localization

Translations exist at two independent levels:

### 1. Shared UI strings (all flavors)

Located in `app/src/main/res/values/strings.xml` (English default) and `app/src/main/res/values-{locale}/strings.xml` per language. Contains app-wide UI text: menu labels, settings, shop, etc.

Each locale file must translate every key from the default `strings.xml` (including per-flavor `app_name_{flavor}` and `suffix_{flavor}`).

### 2. Flavor-specific card names

Located in `app/src/{flavorName}/res/values/strings.xml` (English default) and `app/src/{flavorName}/res/values-{locale}/strings.xml` per language. Contains only card names for that flavor (e.g. `banana`, `apple`).

Each flavor has its own set of translated card names across all supported locales.

### Supported locales

ar, cs, da, de, el, en (default), es, et, fi, fr, hi, hu, in, it, iw, ja, ko, lt, lv, nl, no, pl, pt, ro, ru, sk, sl, sv, tr, uk, vi, zh

The folder code uses Android legacy tags: `in` (Indonesian), `iw` (Hebrew).

### Language display names

Native language names (e.g. "Polski", "Deutsch") are in `main/res/values/strings_non_translatable.xml` as `language_{name}` entries with `translatable="false"`.

### Adding a new language

1. Create `app/src/main/res/values-{locale}/strings.xml` — translate all shared UI strings
2. Create `app/src/{flavorName}/res/values-{locale}/strings.xml` for **each flavor** — translate card names
3. Add `language_{name}` entry (native name, `translatable="false"`) in `main/res/values/strings_non_translatable.xml`
4. Add `LanguageModel(R.string.language_{name}, "{locale}")` to the list in `domain/usecase/GetSupportedLanguagesUseCase.kt`
5. Add the locale to the `SUPPORTED_LANGUAGES` array in `scripts/notifications/send_push_notification.sh`

### Adding translations for a new flavor

For each existing locale, create `app/src/{flavorName}/res/values-{locale}/strings.xml` containing translations of all card names from that flavor's default `values/strings.xml`. Copy the structure from an existing flavor (e.g. `app/src/fruitHalf/res/values-pl/strings.xml`).

## Play Store Listings

Managed via [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher). Listings live at `app/src/{flavor}/play/listings/{locale}/`. Full docs: `docs/docs_listing.md`.

### Character limits

| File | Max length |
|------|-----------|
| `title.txt` | 30 characters |
| `short-description.txt` | 80 characters |
| `full-description.txt` | 4000 characters |
| `video-url.txt` | YouTube URL (no character limit) |

**Always respect these limits when generating or editing listing text.** Titles over 30 chars will be rejected by Play Store.

### Scripts

```bash
./scripts/listing/fetch_listings.sh              # Fetch all flavors from Play Store
./scripts/listing/update_listings.sh              # Push all flavors to Play Store
./scripts/listing/update_listings.sh fruitHalf    # Push specific flavor
```

### Listing structure per locale

```
app/src/{flavor}/play/
├── contact-email.txt
├── contact-website.txt
├── default-language.txt
└── listings/{locale}/
    ├── title.txt
    ├── short-description.txt
    ├── full-description.txt
    ├── video-url.txt
    └── graphics/phone-screenshots/*.jpg
```

## Play Store Screenshots & Feature Graphics

Automated generation of listing screenshots and feature graphics. Scripts in `scripts/screenshot/`.

### Bulk Generation

```bash
./scripts/screenshot/generate_all_screenshots.sh                              # Default: fruit_half mammal_side bird_side × 32 locales
./scripts/screenshot/generate_all_screenshots.sh fruit_half                   # Single flavor × 32 locales
./scripts/screenshot/generate_all_screenshots.sh "fruit_half bird_side" en,pl # Specific flavors + locales
```

Orchestrator that automatically builds + installs each flavor via `./gradlew install{FlavorCamel}Debug`, then runs capture + compose for every locale. Default flavors exclude `vegetable_half` — pass it explicitly if needed.

### Screenshots (5 per locale)

```bash
./scripts/screenshot/take_screenshots.sh fruit_half en                  # Capture 5 raw PNGs from emulator
python3 scripts/screenshot/compose_screenshots.py fruit_half en ~/raw   # Compose final JPEGs with device frames
```

| # | Screen | Layout |
|---|--------|--------|
| 1 | 3×4 Gameplay (2 matched pairs + 1 revealed) | text top, device bottom |
| 2 | Collection (unlocked cards, scrolled to top) | device top, text bottom |
| 3 | 5×6 Gameplay (12 of 15 pairs matched) | text top, device bottom |
| 4 | Daily Challenge End Game (3 stars) | device top, text bottom |
| 5 | Collection (locked cards, scrolled to bottom) | text top, device bottom |

**Design:** Wave layout — devices alternate top/bottom positions with localized lowercase text in the opposite area. 7° rotation, anti-aliased device frames (4x supersample + 2x rotation supersample), per-screenshot layering (main device on top, neighbor edges behind), horizontal offset computed from rotation geometry for equal visual gaps.

Both the regular board (`Random(0)`) and daily challenge board (seed fixed to `0` in RECORDING_MODE) are fully deterministic — card positions are stable across dates.

### Feature Graphics (1024×500)

```bash
python3 scripts/screenshot/generate_feature_graphic.py fruit_half       # All 32 locales
python3 scripts/screenshot/generate_feature_graphic.py fruit_half pl    # Single locale
./scripts/screenshot/generate_feature_graphics.sh                       # All 4 flavors × 32 locales
```

Layout: flavor background color, `ic_logo_{flavor}.png` on left, localized label chips on right (auto-sized font for long translations). Chips use Patrick Hand font with Arial Unicode fallback for CJK/Cyrillic/Arabic.

### Output paths

```
app/src/{flavor}/play/listings/{locale}/graphics/phone-screenshots/{1..5}.jpg
app/src/{flavor}/play/listings/{locale}/graphics/feature-graphic/1.png
```

### Prerequisites
- `RECORDING_MODE = true` in `RecordingModeProvider.kt`
- Pixel 9 Pro emulator (1280×2856) running + connected via ADB
- Python 3 + Pillow (`pip install Pillow`)

## Video Recording for Ads

Automated promo video recording for Google Play Store ads. See `scripts/recording/record_video.sh` for the full script and `docs/docs_recording.md` for detailed documentation.

### Quick start
```bash
./scripts/recording/record_video.sh fruit_half en          # Single video
./scripts/recording/record_all_videos.sh                   # All 4 flavors × 32 languages
./scripts/recording/record_all_videos.sh fruit_half        # All languages for one flavor
```

### Setup
1. Set `RECORDING_MODE = true` in `util/provider/RecordingModeProvider.kt`
2. Build and install all flavors on the **Pixel 2 emulator** (1080x1920, 9:16 — required for Play Store ads)
3. Run the script — it handles demo mode, app data reset, per-app locale (`adb shell cmd locale set-app-locales`), recording, and cleanup
4. Videos are saved to `~/Desktop/memolki_recordings/{flavor}/`

### RECORDING_MODE behavior
When `RECORDING_MODE = true`, the app changes:

| Area | Effect |
|------|--------|
| Click overlay | Shows cursor animation at each tap, blocks rapid multi-taps |
| Card order | Deterministic via fresh `Random(0)` per injection (`di/module/AppModule.kt`, not singleton) |
| Initial state | 20 unlocked cards, 473 coins (via `PrepareRecordingCoinsUseCase`) |
| Casual end game | Hides: Watch Ad, Share button. Shows coins display |
| Daily challenge end game | Hides: Watch Ad. Shows Compare button |
| End game screen | Always shows "Unlock New Card" (even when daily streak available) |
| Collection | Hides Watch Ad unlock (replaced with locked slot to keep total count) |
| Menu | Hides "more apps" section |
| Notifications | Skips notification request screen |
| RTL support | `ForceLtr` helper used for click overlay and board text |

### Post-processing
The script uses `ffmpeg-full` (with freetype) to:
- Speed up video 1.5x
- Add background music (`app/src/main/res/raw/music_background.ogg`) with fade-out at the end
- Add blur overlay fading in at the end
- Add flavor logo (`app/src/main/res/drawable/ic_logo_{flavor}.png`) fading in above the text with circular background
- Add localized "Think you can solve it?" text in Patrick Hand font (`app/src/main/res/font/patrickhand_regular.ttf`), with Arial Unicode fallback for CJK/RTL locales

### RECORDING_MODE guard
Unit tests fail when `RECORDING_MODE = true` — must be set to `false` before merging.

### Flavors and coordinates
Card grid positions are identical across all flavors — fresh `Random(0)` per injection produces the same shuffle order. The script accepts a flavor parameter (`fruit_half`, `vegetable_half`, `mammal_side`, `bird_side`) and resolves the package name automatically.

## Push Notifications (FCM)

Topic-based push notifications via Firebase Cloud Messaging. Users auto-subscribe to a **flavor topic** (e.g. `fruithalf`) and a **language topic** (e.g. `lang_pl`) on app create. Language topic updates on in-app language change.

### Sending notifications

```bash
# Send immediately to all languages
./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt

# Send at 14:00-20:59 in each language's local timezone
./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt --scheduled

# Target a specific flavor
./scripts/notifications/send_push_notification.sh scripts/notifications/example.txt --flavor fruithalf --scheduled
```

### Translations file format (tab-separated)
```
en	Title here	Body here
pl	Tytuł	Treść
```

### Deep links
Notifications can open specific screens via `--screen` and `--board` options:
```bash
./scripts/notifications/send_push_notification.sh translations.txt --screen shop
./scripts/notifications/send_push_notification.sh translations.txt --screen game --board 4x5
```
Screens: `shop`, `collection`, `more_apps`, `game`, `daily_challenge`. Boards (game only): `2x3`, `3x4`, `4x4`, `4x5`, `4x6`, `5x6`, `biggest` (biggest unlocked board — also the default when `--board` is omitted).

Deep link flow: script sends data-only FCM payload → `PushNotificationService.onMessageReceived` creates notification with `ACTION_VIEW` intent → `AppActivity.resolveDeepLinkIntent` converts FCM extras to deep link URI → `AppNavigation.navigateFromDeepLink` routes to the correct screen. Data-only payloads (no `notification` field) are used so `onMessageReceived` is always called regardless of foreground/background state.

Deep link URIs use `DeepLinkBuilder` (`util/notification/DeepLinkBuilder.kt`) as the single source of truth for URI construction and screen constants.

### Local notifications
`NotificationScheduler` (`util/notification/NotificationScheduler.kt`) schedules local alarms via `AlarmManager`:
- Daily challenge reminder at 20:00 local time (re-scheduled after each trigger and on device boot via `BootReceiver`)
- Daily streak reminders and other scheduled notifications

`NotificationAlarmReceiver` (`util/notification/NotificationAlarmReceiver.kt`) handles alarm triggers. For daily challenge, it checks `DailyChallengeRepository.hasPlayed()` before showing the notification — suppresses it if already played today.

### Key files
- `util/notification/PushNotificationService.kt` — receives FCM messages, shows notification with deep link intent
- `util/notification/DeepLinkBuilder.kt` — builds deep link URIs, defines screen/board constants
- `util/notification/NotificationScheduler.kt` — schedules local alarms (daily challenge, streak reminders)
- `util/notification/NotificationAlarmReceiver.kt` — handles alarm triggers, builds and shows notifications
- `util/provider/PushNotificationProvider.kt` — subscribes/unsubscribes FCM topics, tracks previous language via DataStore
- `scripts/notifications/send_push_notification.sh` — sends via FCM v1 API, supports `--scheduled`, `--screen`, `--board`, `--flavor`
- `scripts/notifications/example.txt` — template with all 32 languages

### Config
`FIREBASE_PROJECT_ID` is read from `secrets.properties` (gitignored). Required by the send script. The default FCM notification icon is set to `ic_notification` in `AndroidManifest.xml`.

## CI/CD

- **PR (`.github/workflows/pull_request.yml`):** Runs unit tests with coverage report on pull requests (skips for `chore/` commits)
- **Coverage (`.github/workflows/coverage.yml`):** Generates unit test and screenshot test coverage badges on merge to main (via Gist + shields.io)
- **Merge to main (`.github/workflows/merge.yml`):** Builds release bundles for all flavors and uploads to Google Play, then auto-bumps version and creates a chore PR

## Analytics & Crashlytics

Firebase Analytics and Crashlytics are integrated via `google-services.json` (in `app/`, gitignored). Both are **disabled in debug builds** via `App.kt` (`disableFirebaseInDebug()`) — only release builds send data.

### Analytics
All custom events are logged through `util/analytics/Analytics.kt`. Injected into ViewModels as the **second constructor parameter** (after `savedStateHandle`). In tests, provided as `relaxedMockk()` from `TestModule` — verify calls with `verify { analytics.logX() }`.

Event names, parameter keys, and values are organized in `private object Event`, `private object Key`, and `private object Value` inside `Analytics.kt`. Callers use typed helper methods (e.g. `logAdRewardFromShop()`, `logCardUnlockedWithCoins()`) — no raw string literals at call sites.

User properties: `setUserLanguage()` sets a Firebase user property (not an event).

### Crashlytics
Non-fatal errors are reported via `logE()` in `util/extension/AnyExtensions.kt` — every `logE` call records to both Logcat and `FirebaseCrashlytics.recordException()`. This covers all base use case errors (via `BaseUseCase.catch`) and ViewModel failure handlers.

### Use case error handling
Use cases must **NOT** use `runCatching` to wrap their logic. The base class (`BaseUseCase` / `BaseParameterUseCase`) already has a `.catch` block that wraps exceptions in `Result.failure()` and logs them via `logE` (which reports to Crashlytics). Using `runCatching` silently swallows errors and prevents Crashlytics from seeing them.

## Code Style

- **No trailing commas** — do NOT add a comma after the last parameter in function declarations, constructor parameters, or function calls. The linter will remove them.
- **No unnecessary blank lines** — do not add blank lines inside function bodies, between consecutive property declarations, or between tightly related statements. One blank line is fine between top-level declarations (functions, classes) and between logical sections, but avoid multiple consecutive blank lines or blank lines that add no readability value.
- **Small, focused functions** — extract logic into small, well-named private functions rather than writing long blocks with deep nesting. Prefer flat, readable code with minimal indentation levels.
- **Every file ends with a single newline** — always ensure exactly one trailing newline at the end of every file.

## Key Conventions

- Kotlin 2.3.20, JVM target 11, Compose enabled — versions managed via `gradle/libs.versions.toml`
- Min SDK 23, Target/Compile SDK 36
- Version format: MAJOR.MINOR.PATCH (versionCode encodes as MMMNNNPPP)
- Product flavors use the "version" dimension; each flavor has its own package name and billing key

## Common Mistakes

Recurring errors to avoid — check this list before submitting changes.

### Build & Gradle
- **Wrong task name casing:** Gradle tasks use camelCase flavor names — `installFruitHalfDebug`, NOT `installFruit_halfDebug`
- **Forgetting flavor in test commands:** Always specify flavor — `testFruitHalfDebugUnitTest`, not bare `test`

### RECORDING_MODE
- **Leaving RECORDING_MODE = true:** Unit tests fail when `RECORDING_MODE = true`. Always set back to `false` before merging
- **Navigation with KEYCODE_BACK:** Pressing BACK too many times exits the app to the home screen. Count back presses carefully — one BACK from a game goes to choose board or menu, two exits the app

### Architecture
- **No `runCatching` in use cases:** Base class already handles exceptions and reports to Crashlytics
- **No `delay()` for UI timing in ViewModels:** Let the UI send an intent when animation completes
- **No Android framework calls in ViewModels/UseCases:** Wrap in Provider classes under `util/provider/`
- **`first()` is fine for one-shot BaseUseCase flows:** `BaseUseCase.execute()` emits exactly once; `.first()` is idiomatic for these. Only avoid `first()` on continuous flows (DataStore, combine, etc.)
- **No `getOrNull()` / `getOrThrow()`:** Handle results with `.onSuccess { }` / `.onFailure { }`
- **Compose use cases with sequential `first()`, not `combine()`:** Nesting `combine()` inside `BaseUseCase.execute()` creates layered `flowOn` chains that break `StandardTestDispatcher` in tests. Use `flow { val x = otherUseCase().first().getOrThrow(); ... }` instead (see `UnlockRandomCardIfEnoughCoinsUseCase`)
- **Don't hack production code to fix test issues:** If `relaxedMockk` swallows a call, stub it properly in `TestModule` — don't convert member functions to extensions or restructure production code as a workaround
- **Fix root causes, don't scatter suppressions:** If a warning appears in many files, fix the source (e.g. `const val` → `val`) instead of adding `@Suppress` everywhere

### Testing
- **Always add tests for new UseCases/ViewModels:** Every new UseCase and ViewModel must have a test class — don't skip this step

### Play Store
- **Feature graphic uses existing assets as source:** Don't use old feature graphics (they bake in English text). Use `ic_logo_{flavor}.png` from `res/drawable/` as the clean logo source
- **Character limits are strict:** title.txt = 30 chars, short-description.txt = 80 chars, full-description.txt = 4000 chars
