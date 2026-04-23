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

Memolki is an Android card-matching memory game built with Jetpack Compose. It ships as multiple app flavors (each with a unique theme, package name, and billing key). Check `androidApp/build.gradle.kts` for the current list of flavors under `productFlavors`.

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

The app lives in the `:androidApp` Gradle module (shared KMP-ready code will live in `:shared`). Task names below work unqualified (no other module exposes `assembleFruitHalfDebug` etc.), but can be prefixed with `:androidApp:` for clarity.

```bash
# Build debug APK
./gradlew :androidApp:assembleFruitHalfDebug

# Install debug APK on connected device/emulator and launch it
./gradlew :androidApp:installFruitHalfDebug && adb shell am start -n com.wojdor.memolki.fruithalf/com.wojdor.memolki.ui.app.AppActivity

# Run all unit tests — you MUST specify a flavor, the unqualified task name is ambiguous
./gradlew :androidApp:testFruitHalfDebugUnitTest

# Run a single test class
./gradlew :androidApp:testFruitHalfDebugUnitTest --tests "com.wojdor.memolki.ui.feature.menu.MenuViewModelTest"

# Run a single test method
./gradlew :androidApp:testFruitHalfDebugUnitTest --tests "com.wojdor.memolki.ui.feature.menu.MenuViewModelTest.when initial load is done then the state is updated with menu"

# Verify screenshot tests — run after building/installing to catch visual regressions
./gradlew :androidApp:verifyPaparazziFruitHalfDebug

# Record new screenshot references — run after intentional UI changes
./gradlew :androidApp:recordPaparazziFruitHalfDebug

# Generate unit test coverage report (excludes screenshot tests)
./gradlew :androidApp:koverXmlReportFruitHalfDebug -PcoverageTestExclude='com.wojdor.memolki.screenshot.*'

# Generate screenshot test coverage report
./gradlew clean :androidApp:koverXmlReportFruitHalfDebug -PcoverageTestFilter='com.wojdor.memolki.screenshot.*'
```

After making UI changes, always run `verifyPaparazziFruitHalfDebug` alongside the build/install step. If screenshots differ intentionally, run `recordPaparazziFruitHalfDebug` to update the references and commit the updated PNGs.

## Setup

A `secrets.properties` file is needed in the root directory with:
- `<FLAVOR_UPPER_SNAKE>_BILLING_KEY` entry for each flavor (see `androidApp/build.gradle.kts`)
- `FIREBASE_PROJECT_ID` — required by `scripts/notifications/send_push_notification.sh`

Adding a new flavor has a checklist in `docs/docs_new_app_flavor_setup.md`.

## Architecture

**MVI + Clean Architecture** with three layers, across two Gradle modules:

```
UI (Compose) → Domain (Use Cases) → Data (Repositories → DataSources)
```

- `:androidApp` hosts the Android app (Activity, manifest, services, receivers, flavor source sets, Paparazzi).
- `:shared` is a Kotlin Multiplatform library (`commonMain` / `androidMain` / `iosMain`) that holds portable code. An iOS app target is scaffolded separately in phase 14.
- Code lives in `:shared/commonMain` when it can be authored once and consumed on both platforms; in `:shared/androidMain` (or `:shared/iosMain`) when a platform-specific `actual` is needed for an `expect` declaration; in `:androidApp` when it's genuinely Android-only (UI composables, manifest-bound receivers, flavor data).

**Layer boundaries are strict:** Use cases depend only on repositories (and other use cases) — never import data-layer internals (`data/local/database/` entities, `data/mapper/`). Repositories are responsible for mapping between data entities and domain models.

### UseCase Layer

Two base classes in `domain/usecase/base/` (located in `:shared/commonMain`):
- `BaseUseCase<R>` — no-parameter use case, invoked as `useCase()`
- `BaseParameterUseCase<P, R>` — parameterized, invoked as `useCase(param)`

Both return `Flow<Result<R>>` via `operator fun invoke()`. Override `execute()` to implement.

Conventions:
- Named `[Verb][Noun]UseCase` (e.g. `GetSettingsUseCase`, `ToggleSettingsUseCase`)
- Plain constructor — no `@Inject` (Koin wires dependencies by type from `AppKoinModule`)
- **Dispatcher is always the 1st constructor parameter** — type `CoroutineDispatcher`
- Dispatcher choice: unqualified (defaults to `Dispatchers.IO`) for anything touching system services, repositories, or I/O; inject `get(DefaultDispatcher)` for pure in-memory computation; `get(MainDispatcher)` only when the API requires the main thread. The `DefaultDispatcher` and `MainDispatcher` named qualifiers are defined in `di/AppKoinModule.kt` alongside the module.
- **Do NOT use `runCatching`** — the base class `.catch` block handles exceptions and reports to Crashlytics (see Analytics & Crashlytics section)

Reference examples:
- Simple: `shared/src/commonMain/.../domain/usecase/GetCoinsUseCase.kt`
- With parameter: `shared/src/commonMain/.../domain/usecase/ToggleSettingsUseCase.kt`

Six daily-challenge use cases still live in `:androidApp` because they depend on `TimeProvider` (which uses `java.time`): `CheckDailyLoginStreakUseCase`, `CollectDailyStreakRewardUseCase`, `GetDailyChallengeCardsUseCase`, `GetTodayDailyChallengeUseCase`, `HasPlayedTodayDailyChallengeUseCase`, `SaveDailyChallengeUseCase`. They move once `TimeProvider` migrates to `kotlinx.datetime`.

### Domain Models

Located in `:shared/commonMain/.../domain/model/`. All domain models have moved: `AppModel`, `BoardModel`, `CardModel`, `CardPairModel`, `CollectionCardPairModel`, `DailyChallengeModel`, `EndGameMenuModel`, `LanguageModel`, `MenuModel`, `SettingModel`, `ShopMenuModel`, plus `StarCalculator`.

Sealed classes with `@Serializable`, often including:
- Abstract properties in the sealed parent
- Data class subclasses for concrete types (each also annotated `@Serializable`)
- `object Empty` sentinel for default/initial values (also `@Serializable`)
- Resource IDs: `StringResource` / `DrawableResource` / `Color` typed fields serialize via `StringResourceSerializer` / `DrawableResourceSerializer` / `ColorSerializer` in `:shared/commonMain/util/serializer/` (strings encode via `StringResource.key`, drawables via reverse-lookup in `Res.allDrawableResources`, colors via underlying `ULong` value). Apply via `@file:UseSerializers(...)` at the top of each model file — no per-field annotation. `CardModel.imageRes` still holds `Int` (flavor-specific card drawables pending migration to composeResources flavor source sets).

Reference: `shared/src/commonMain/.../domain/model/CardModel.kt`

### Data Layer

- **Persistence:** Two storage mechanisms (both live in `:shared/commonMain`):
  - **Encrypted DataStore Preferences** (`data/local/datastore/`) — for key-value settings and user data (coins, streaks, unlocked cards). Uses `data/crypto/Encryptor` (interface in `:shared/commonMain`, Android impl `BaseEncryptor` in `:androidApp`) for sensitive values. Never use SharedPreferences.
  - **Room Database** (`data/local/database/`) — for structured, growing data (e.g. daily challenge results). `AppDatabase` in `data/local/database/`, entities and DAOs in subdirectories. Registered in `AppKoinModule`. Database version tracked as `private const val DATABASE_VERSION` in `AppDatabase.kt`. When bumping `DATABASE_VERSION`, add a proper migration to preserve user data.
- **Entities:** DataStore entities in `data/entity/`, mapped via `data/mapper/`. Room entities live next to their DAOs in `data/local/database/`.
- **Repositories:** in `:shared/commonMain/data/repository/`, orchestrate data sources from `data/local/`. Repositories map between data entities and domain models — never expose Room entities or DataStore keys in the public API
- **Backup & Restore:** `res/xml/backup_rules.xml` (API < 31) and `res/xml/data_extraction_rules.xml` (API 31+) include the `sharedpref` and `database` domains, plus the `file` domain with `path="datastore/"`. When adding a new persistence mechanism, update both files

### MVI Pattern

Each feature screen follows this structure under `ui/feature/{name}/`. The MVI data classes (`State`/`Intent`/`Effect`/`Callbacks`) live in `:shared/commonMain` so they can drive both platforms; `ViewModel` and `Screen` currently still live in `:androidApp`. `EndGameEffect` is the only Effect still in `:androidApp` — it references `ReviewManager`/`ReviewInfo` and moves once that's abstracted.

| File | Module | Purpose |
|---|---|---|
| `{Name}State.kt` | `:shared/commonMain` | `@Serializable` data class implementing `UiState`, all properties have defaults |
| `{Name}Intent.kt` | `:shared/commonMain` | Sealed class implementing `UiIntent`, entries named `On[Action]` |
| `{Name}Effect.kt` | `:shared/commonMain` | Sealed class implementing `UiEffect` for one-shot side effects (navigation, toasts, showing overlays) |
| `{Name}Callbacks.kt` | `:shared/commonMain` | (optional) Data class grouping lambdas for the screen, defaults to `= {}` |
| `{Name}ViewModel.kt` | `:androidApp` | Plain class extending `MviViewModel<Intent, State>` — bound in `AppKoinModule` via `viewModelOf(::{Name}ViewModel)` |
| `{Name}Screen.kt` | `:androidApp` | `@Composable` with three-level hierarchy (see below) |

**Base class:** `MviViewModel` (`shared/src/commonMain/.../ui/base/MviViewModel.kt`) manages intent→state flow via `sendIntent()`, `onIntent()`, `sendState { copy(...) }`, `sendEffect()`. State is persisted through `SavedStateHandle` as a JSON string via `kotlinx.serialization` — each ViewModel passes `FooState.serializer()` into its `super(...)` call. Malformed saved JSON (e.g. after a state-schema change) falls back to the initial state. Lives in commonMain thanks to multiplatform `androidx.lifecycle` artifacts.

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
    viewModel: {Name}ViewModel = koinViewModel(),
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
- Use test drawables (`Res.drawable.img_test_whole`, `Res.drawable.img_test_half`) for image previews
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

Structure — most providers are multiplatform via `expect/actual`:
- **`expect open class`** in `shared/src/commonMain/.../util/provider/` declares the API shape
- **`actual open class`** in `shared/src/androidMain/...` with the real Android implementation (takes `Context` in constructor where needed, wired via `get()` in Koin)
- **`actual open class`** in `shared/src/iosMain/...` — stub implementation. Each stub carries a `TODO(ios):` marker pointing at the real iOS API to use later (e.g. `UNUserNotificationCenter`, `NSLocale`, `UIApplication.canOpenURL`).

Providers currently on expect/actual: `AppForegroundProvider`, `AppInstalledProvider`, `LocaleProvider`, `PackageNameProvider`, `PermissionProvider`. `RecordingModeProvider` is a plain `object` in `commonMain` (single `const val`, no platform diff). `TimeProvider` and `PushNotificationProvider` still live in `:androidApp` — `TimeProvider` moves after the `java.time → kotlinx.datetime` swap; `PushNotificationProvider` is staying Android-only since it wraps `FirebaseMessaging` topic subscription and Android-specific language-tag handling.

### Platform services (Billing / Ads / Notifications / PlayGames)

Bigger Android-only subsystems are exposed to ViewModels as **commonMain interfaces** with Android impls in `:androidApp` and iOS no-op impls in `shared/iosMain`. ViewModels and use cases never touch `Activity`, `ProductDetails`, `PendingIntent`, etc. directly — the Android impl tracks the current `Activity` internally via `ActivityLifecycleCallbacks` where needed.

| Interface (commonMain) | Android impl (`:androidApp`) | iOS stub (`shared/iosMain`) |
|---|---|---|
| `util/billing/BillingHandler` + `BillingProduct` + `BillingStatusListener` | `AndroidBillingHandler` (Play Billing, tracks Activity) | `NoopBillingHandler` |
| `ui/ads/RewardedAd` + `ui/ads/AllRewardedAds` | `AndroidRewardedAd`/`AndroidAllRewardedAds` (AdMob). `show(activity, …)` is an Android-only extension in `ui/ads/RewardedAdExtensions.kt` | `NoopAllRewardedAds` |
| `util/notification/NotificationScheduler` | `AndroidNotificationScheduler` (`AlarmManager` + lifecycle observer) | `NoopNotificationScheduler` |
| `util/playgames/GooglePlayGames` | `AndroidGooglePlayGames` (Play Games SDK, tracks Activity) | `NoopGooglePlayGames` |

`InAppUpdate` stays Android-only — only `AppActivity` calls it, and iOS relies on App Store, so no common interface.

MVI effects only carry common types (`BillingProduct`, `Long`, `RewardedAd` interface) — they no longer ship concrete Android objects to screens. Screens inject `BillingHandler`/`GooglePlayGames` via `koinInject<>()` when handling effects.

Testing:
- Each provider has a corresponding `Fake{Name}` class in `shared/src/androidMain` OR `androidApp/src/test/fake/` (currently still all in `androidApp/test/fake/`) that extends the Android `actual` class, passing `mockk()` as the context where needed
- Fakes are **fully functional** — they hold state via private vars and override all methods with real behavior (no mocks, no no-ops)
- Fakes are bound in `TestKoinModule` via `singleOf(::FakeFoo) { bind<FooProvider>() }`
- Tests read the fake via `val fakeLocaleProvider: FakeLocaleProvider by inject()` and use it to control state directly. `FooProvider by inject()` is also valid when the test only needs the public contract.

Examples: `LocaleProvider` (commonMain expect + androidMain/iosMain actuals) / `FakeLocaleProvider` (androidApp/test/fake, extends Android actual)

### Dependency Injection

**Koin** — no annotations, wired by constructor types. All bindings declared in `androidApp/src/main/.../di/AppKoinModule.kt`:
- 41 `factoryOf(::FooUseCase)` for IO-dispatcher use cases (auto-wires because the unqualified `CoroutineDispatcher` binding is `Dispatchers.IO`)
- Explicit `factory { FooUseCase(get(DefaultDispatcher), get(), …) }` for the ~15 use cases that need `@DefaultDispatcher` / `@MainDispatcher`
- `viewModelOf(::FooViewModel)` for the 13 ViewModels
- `singleOf(::FooProvider)` / `singleOf(::FooRepository)` for providers, repositories, data sources, util classes, framework singletons

`App.kt` starts Koin:
```kotlin
startKoin {
    androidContext(this@App)
    modules(appKoinModule)
}
```

`AppActivity`, `PushNotificationService`, `NotificationAlarmReceiver`, `BootReceiver` implement `KoinComponent` and use `by inject()` / `by viewModel()` for field-style access.

Coroutine dispatchers:
- **Unqualified** `CoroutineDispatcher` → `Dispatchers.IO` (default)
- `DefaultDispatcher` named qualifier → `Dispatchers.Default`
- `MainDispatcher` named qualifier → `Dispatchers.Main`
- Never hardcode `Dispatchers.*` directly in use cases / ViewModels — always inject.

### UI Utilities

- **Responsive spacing:** `shared/src/commonMain/.../ui/theme/Dimensions.kt` — `spacingXS`/`S`/`M`/`L`/`XL` are composable properties that adapt based on `isLargeScreen` (LocalScreenWidth >= 600.dp) and `isSmallScreen` (LocalScreenHeight < 750.dp). `AppActivity` provides both via `CompositionLocalProvider` from `LocalConfiguration`. No Jetpack `WindowSizeClass` dependency — keeps Dimensions in commonMain.
- **Click throttling:** `util/ClickUtils.kt` — `throttleClick()` composable wrapper prevents duplicate clicks (1s default)
- **Logging:** `shared/src/commonMain/.../util/extension/Logger.kt` — `Any.logD()` / `Any.logE()` use the class name as tag. `logE()` also reports to Firebase Crashlytics (via GitLive) as a non-fatal exception

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

- **Framework:** JUnit 4 + Turbine (Flow testing) + MockK + Koin Test (`KoinTest`)
- **Base class:** `AppTest` (`test/AppTest.kt`) — implements `KoinTest`; starts Koin with `testKoinModule` in `@Before` and `stopKoin()` in `@After`; sets `Dispatchers.setMain(testDispatcher)` with the injected `StandardTestDispatcher`; initializes MockK annotations.
- **Test DI:** `TestKoinModule` (`test/di/TestKoinModule.kt`) mirrors `AppKoinModule` with fakes (`FakeEncryptor`, `FakeLocaleProvider`, `FakeTimeProvider`, `FakeNotificationScheduler`, etc. bound to their interfaces) + `relaxedMockk()` for platform dependencies tests rarely exercise directly (`HapticFeedback`, `BillingHandler` (interface), `Analytics`, `GooglePlayGames` (interface), Firebase, media players, `AllRewardedAds` (interface)). `Random(0)` for deterministic randomness. One `StandardTestDispatcher` shared across IO / `DefaultDispatcher` / `MainDispatcher` qualifiers so `runTest` and `Dispatchers.setMain` stay aligned.
- **Test utilities:** `test/TestUtils.kt` — `relaxedMockk<T>()`, `verifyOnce()`, `coVerifyOnce()`
- **Test naming:** backtick-quoted descriptive names: `` `when X then Y` ``

Conventions:
- Extend `AppTest`, annotate with `@ExperimentalCoroutinesApi`
- Read Koin-managed dependencies with `private val foo: Foo by inject()`; use `@MockK` / `@RelaxedMockK` only for mocks local to the test
- Construct SUT in `@Before setup()` (after `super.setup()` so Koin is started first)
- Wrap tests in `runTest {}`, assert flows with Turbine's `.test { awaitItem() }`
- For one-shot flows (e.g. `flow { emit(...) }`), call `awaitComplete()` after consuming items to avoid "Unconsumed events" errors
- For long-lived flows (e.g. DataStore, StateFlow), no `awaitComplete()` needed
- Comments in tests are **only** `// given`, `// when`, `// then` — no other comments (no inline explanations, no `// region`/`// endregion`, no trailing descriptions)
- Use `assertTrue()`/`assertFalse()` — never `assertEquals(true, ...)` or `assertEquals(false, ...)`
- For static Android APIs (e.g. `AppCompatDelegate`), use `mockkStatic(...)` in `@Before`
- **Fake scoping:** Koin `singleOf(::FakeFoo) { bind<Foo>() }` binds the fake as a singleton behind both the concrete Fake class AND the interface/parent class. `by inject()` returns the same instance each time, so injecting `FakeLocaleProvider` and `LocaleProvider` in the same test yields the same object — no risk of divergent instances. When testing a UseCase that depends on other UseCases that use Fakes, use the Koin-provided instance instead of constructing child UseCases manually.

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

Each flavor lives under `androidApp/src/{flavorName}/` and contains:

```
androidApp/src/{flavorName}/
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
1. Add flavor to `flavorConfigs` in `androidApp/build.gradle.kts`
2. Add package name to `<queries>` in `AndroidManifest.xml`
3. Add `object` entry in `domain/model/AppModel.kt` and include it in `all()`
4. Add `app_name_{flavor}` and `suffix_{flavor}` strings in `main/res/values/strings.xml`
5. Add `primary_{flavor}` color in `main/res/values/colors.xml`
6. Create the flavor source set directory with the structure above
7. Create `AllCardPairsLocalDataSource.kt` with card pair definitions (reference: `androidApp/src/fruitHalf/`)
8. Create `res/xml/shortcuts.xml` with the flavor's `applicationId` in `targetPackage` (copy from existing flavor)
9. Add flavor to CI matrix in `.github/workflows/merge.yml`

### Card Images

Named `img_{name}_whole.jpg` and `img_{name}_half.jpg` (or `_side`/`_front` depending on flavor theme), provided at 5 density buckets (1024, 720, 512, 384, 256px square, JPEG quality 50).

Card names for each flavor are listed in `list/{flavor}.txt` (one per line, human-readable).

To generate and resize images use `scripts/image_generation/assist.py` — it walks through each card, copies the name to clipboard for pasting into Gemini, watches `~/Downloads` for the result, and auto-resizes to all densities. See `docs/docs_images.md` for details.

## Localization

Translations exist at two independent levels:

### 1. Shared UI strings (all flavors)

Canonical (and only) location is `:shared/src/commonMain/composeResources/values/strings.xml` (English default) and `:shared/src/commonMain/composeResources/values-{locale}/strings.xml` per language. Use `stringResource(Res.string.foo)` from `org.jetbrains.compose.resources` (import `com.wojdor.memolki.shared.resources.*`). The Res class lives at `com.wojdor.memolki.shared.resources.Res`. Non-translatable UI entries (language names, board size labels, `empty`, `app_logo`, `new_card_to_unlock`) live in `:shared/src/commonMain/composeResources/values/strings_non_translatable.xml`.

`androidApp/src/main/res/values*/strings.xml` now keeps only strings still consumed by Android-only code: manifest refs (`app_name`, `app_name_{flavor}`, `ad_mob_app_id`, `game_id`), PlayGames IDs (`leaderboard_*_id`), launcher shortcuts (`shortcut_daily_reward`, `shortcut_play`), notification arrays/plurals/channel, `CardModel` sentinels / Android-side toast+share strings (`empty`, `level_count`, `menu`, `new_game`, `share_casual`, `daily_reward_*`, `shop_*`, `watch_ad`, `notification_channel_reminders`), and the `ad_mob_*` unit IDs. Everything else has been deleted to eliminate drift.

Each locale file must translate every key from the default `strings.xml`.

### 2. Flavor-specific card names

Located in `:shared/src/android{Flavor}/composeResources/values/strings.xml` (English default) and `:shared/src/android{Flavor}/composeResources/values-{locale}/strings.xml` per language — one set per flavor (`androidFruitHalf`, `androidVegetableHalf`, `androidMammalSide`, `androidBirdSide`). Consumed via `Res.string.apple`, `Res.string.banana`, etc. from `AllCardPairsLocalDataSource` in each `androidApp/src/{flavor}/java/.../data/local/card/` source set.

`androidApp/src/{flavor}/res/values*/strings.xml` now only carries the single `app_name` alias (`@string/app_name_{flavor}`) that the manifest consumes.

### 3. Shared drawables

Canonical location is `:shared/src/commonMain/composeResources/drawable/` and `…/drawable-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/`. Use `painterResource(Res.drawable.foo)` from `org.jetbrains.compose.resources`. The same files remain duplicated in `androidApp/src/main/res/drawable*/` until phase 13c removes them — update both copies when adding or changing a shared drawable. Kept Android-only in `androidApp/src/main/res/drawable*/`: `ic_notification` (manifest), `ic_launcher_monochrome`, `ic_shortcut_daily_reward*`, `ic_logo_splashscreen` (splash theme), and flavor logos `ic_logo_{flavor}` referenced from `AppModel`.

### Supported locales

ar, cs, da, de, el, en (default), es, et, fi, fr, hi, hu, in, it, iw, ja, ko, lt, lv, nl, no, pl, pt, ro, ru, sk, sl, sv, tr, uk, vi, zh

The folder code uses Android legacy tags: `in` (Indonesian), `iw` (Hebrew).

### Language display names

Native language names (e.g. "Polski", "Deutsch") are in `main/res/values/strings_non_translatable.xml` as `language_{name}` entries with `translatable="false"`.

### Adding a new language

1. Create `:shared/src/commonMain/composeResources/values-{locale}/strings.xml` — translate all shared UI strings. Only mirror into `androidApp/src/main/res/values-{locale}/strings.xml` for the small set of keys still used from Android-side code (see the shared UI strings section above).
2. Create `androidApp/src/{flavorName}/res/values-{locale}/strings.xml` for **each flavor** — translate card names
3. Add `language_{name}` entry (native name, `translatable="false"`) in `:shared/src/commonMain/composeResources/values/strings_non_translatable.xml`
4. Add `LanguageModel(R.string.language_{name}, "{locale}")` to the list in `domain/usecase/GetSupportedLanguagesUseCase.kt`
5. Add the locale to the `SUPPORTED_LANGUAGES` array in `scripts/notifications/send_push_notification.sh`

### Adding translations for a new flavor

For each existing locale, create `androidApp/src/{flavorName}/res/values-{locale}/strings.xml` containing translations of all card names from that flavor's default `values/strings.xml`. Copy the structure from an existing flavor (e.g. `androidApp/src/fruitHalf/res/values-pl/strings.xml`).

## Play Store Listings

Managed via [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher). Listings live at `androidApp/src/{flavor}/play/listings/{locale}/`. Full docs: `docs/docs_listing.md`.

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
androidApp/src/{flavor}/play/
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
androidApp/src/{flavor}/play/listings/{locale}/graphics/phone-screenshots/{1..5}.jpg
androidApp/src/{flavor}/play/listings/{locale}/graphics/feature-graphic/1.png
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
- Add background music (`androidApp/src/main/res/raw/music_background.ogg`) with fade-out at the end
- Add blur overlay fading in at the end
- Add flavor logo (`androidApp/src/main/res/drawable/ic_logo_{flavor}.png`) fading in above the text with circular background
- Add localized "Think you can solve it?" text in Patrick Hand font (`androidApp/src/main/res/font/patrickhand_regular.ttf`), with Arial Unicode fallback for CJK/RTL locales

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

Deep link URIs use `DeepLinkBuilder` (`shared/.../util/notification/DeepLinkBuilder.kt`, commonMain object) as the single source of truth for URI construction and screen constants.

### Local notifications
`NotificationScheduler` is a **commonMain interface** (`shared/.../util/notification/NotificationScheduler.kt`). Callers (use cases, ViewModels) only see this interface; the Android impl `AndroidNotificationScheduler` (in `:androidApp`) schedules alarms via `AlarmManager`. On iOS a `NoopNotificationScheduler` in `shared/iosMain` keeps commonMain callers compiling. AndroidNotificationScheduler additionally implements `DefaultLifecycleObserver` so `AppActivity` can register it against the lifecycle — AppActivity injects the Android type directly.

Scheduled work:
- Daily challenge reminder at 20:00 local time (re-scheduled after each trigger and on device boot via `BootReceiver`)
- Daily streak reminders and other scheduled notifications

`NotificationAlarmReceiver` (`androidApp/.../util/notification/NotificationAlarmReceiver.kt`) handles alarm triggers. For daily challenge, it checks `DailyChallengeRepository.hasPlayed()` before showing the notification — suppresses it if already played today.

### Key files
- `shared/src/commonMain/.../util/notification/NotificationScheduler.kt` — common interface (+ `SHOP_AD_COOLDOWN_MS`)
- `shared/src/commonMain/.../util/notification/DeepLinkBuilder.kt` — pure-Kotlin URI builder, screen/board constants
- `shared/src/iosMain/.../util/notification/NoopNotificationScheduler.kt` — iOS stub
- `androidApp/.../util/notification/AndroidNotificationScheduler.kt` — Android impl (`AlarmManager` + `DefaultLifecycleObserver`; holds `TYPE_*` / `*_NOTIFICATION_ID` constants)
- `androidApp/.../util/notification/NotificationCreator.kt` — Android-only `NotificationManager` wrapper (receivers call directly, no interface)
- `androidApp/.../util/notification/PushNotificationService.kt` — receives FCM messages, shows notification with deep link intent
- `androidApp/.../util/notification/NotificationAlarmReceiver.kt` — handles alarm triggers, builds and shows notifications
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

Firebase Analytics and Crashlytics are integrated via `google-services.json` (in `androidApp/`, gitignored). The common Kotlin SDK is **GitLive Firebase** (`dev.gitlive:firebase-analytics`, `dev.gitlive:firebase-crashlytics`) declared in `:shared`'s `commonMain` so both layers are KMP-ready; GitLive delegates to the Google Android SDKs under the hood. Both are **disabled in debug builds** via `App.kt` (`disableFirebaseInDebug()`) — only release builds send data.

### Analytics
All custom events are logged through `shared/src/commonMain/.../util/analytics/Analytics.kt`. Injected into ViewModels as the **second constructor parameter** (after `savedStateHandle`). In tests, provided as `relaxedMockk()` from `TestKoinModule` — verify calls with `verify { analytics.logX() }`.

Event names, parameter keys, and values are organized in `private object Event`, `private object Key`, and `private object Value` inside `Analytics.kt`. Callers use typed helper methods (e.g. `logAdRewardFromShop()`, `logCardUnlockedWithCoins()`) — no raw string literals at call sites. Parameters are passed as `Map<String, Any>` to GitLive's `logEvent` (no Android `Bundle`).

Board-scoped helpers (`logBoardStart`, `logBoardComplete`, `logBoardAbandoned`) take `columns: Int, rows: Int` rather than a `BoardModel`, so the whole file stays in `commonMain` until `BoardModel` itself moves (Phase 13).

User properties: `setUserLanguage()` sets a Firebase user property (not an event).

### Crashlytics
Non-fatal errors are reported via `logE()` in `shared/src/commonMain/.../util/extension/Logger.kt`. The Android `actual` records to both Logcat and GitLive's `Firebase.crashlytics.recordException()`. The Crashlytics call is wrapped in `runCatching` so JVM unit tests (where Firebase isn't initialized) don't crash. This covers all base use case errors (via `BaseUseCase.catch`) and ViewModel failure handlers.

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
- **Don't hack production code to fix test issues:** If `relaxedMockk` swallows a call, stub it properly in `TestKoinModule` — don't convert member functions to extensions or restructure production code as a workaround
- **Fix root causes, don't scatter suppressions:** If a warning appears in many files, fix the source (e.g. `const val` → `val`) instead of adding `@Suppress` everywhere

### Testing
- **Always add tests for new UseCases/ViewModels:** Every new UseCase and ViewModel must have a test class — don't skip this step

### Play Store
- **Feature graphic uses existing assets as source:** Don't use old feature graphics (they bake in English text). Use `ic_logo_{flavor}.png` from `res/drawable/` as the clean logo source
- **Character limits are strict:** title.txt = 30 chars, short-description.txt = 80 chars, full-description.txt = 4000 chars
