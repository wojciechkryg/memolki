# AGENTS.md

This file provides guidance to AI coding agents when working with code in this repository.

## Project Overview

Memolki is an Android card-matching memory game built with Jetpack Compose. It ships as multiple app flavors (each with a unique theme, package name, and billing key). Check `app/build.gradle.kts` for the current list of flavors under `productFlavors`.

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
```

## Setup

A `secrets.properties` file is needed in the root directory with a `<FLAVOR_UPPER_SNAKE>_BILLING_KEY` entry for each flavor. See `app/build.gradle.kts` for how these are read.

Adding a new flavor has a checklist in `docs/docs_new_app_flavor_setup.md`.

## Architecture

**MVI + Clean Architecture** with three layers:

```
UI (Compose) → Domain (Use Cases) → Data (Repositories → DataSources)
```

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
- Wrap repository/data calls in `runCatching { }` so exceptions are emitted as `Result.failure` instead of crashing the flow

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

- **Persistence:** Encrypted DataStore Preferences (see `data/crypto/` for Encryptor interface)
- **Entities:** in `data/entity/`, mapped to domain models via extension functions in `data/mapper/` (e.g. `CardEntity.toModel()`)
- **Repositories:** in `data/repository/`, orchestrate data sources from `data/local/`

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
- Handle results with `.onSuccess { }` / `.onFailure { }` — never use `getOrNull()` or `first()` on use case flows (use case flows may emit multiple values over time, and `first()` silently drops updates; `getOrNull()` discards error information)
- **No `delay()` for UI timing** — if the ViewModel needs to wait for an animation, let the UI send an intent when the animation completes instead
- **No Android framework calls** in ViewModels or UseCases (e.g. `AppCompatDelegate`, `Context`, `LocaleManager`) — wrap them in Provider classes under `util/provider/`
- **No redundant state** — don't create separate `var` flags when a state field already covers the same purpose
- **Strict MVI communication** — View ↔ ViewModel communication only through State, Effect, and Intent. Never expose public properties or functions on ViewModels beyond `sendIntent()` and what `MviViewModel` provides
- **Effects vs State** — Effects are for one-time events that the UI cannot derive from State (navigation, toasts, launching external intents). State is for anything that drives the UI (including flags like `isLanguageChangeInProgress` that control visibility, animations, or overlays). Never use an Effect to set local composable `remember` state — if the ViewModel knows about a condition, put it in State and let the UI observe it directly
- ViewModels survive activity recreation (config changes, locale changes)

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
- **Game flow:** ChooseLevel → Game → EndGame (ViewModels shared via flow scope)
- **Collection flow:** Collection → Shop → CardPairDetails
- **Settings flow:** Settings → ChangeLanguage

Shared ViewModels within a flow are scoped to the navigation graph's back stack entry (see `getGameViewModel()` pattern).

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
- `DataModule.kt` — repositories and data sources

Coroutine dispatchers are injected via qualifiers defined in `di/coroutine/`: `@DefaultDispatcher`, `@IoDispatcher`, `@MainDispatcher`. Never hardcode `Dispatchers.*` directly.

### UI Utilities

- **Responsive spacing:** `ui/theme/Dimensions.kt` — `spacingXS`/`S`/`M`/`L`/`XL` are composable properties that adapt based on `isTablet` (WindowSizeClass)
- **Click throttling:** `util/ClickUtils.kt` — `throttleClick()` composable wrapper prevents duplicate clicks (1s default)
- **Logging:** `util/extension/` — `Any.logD()` / `Any.logE()` use the class name as tag

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
- Comments follow `// given`, `// when`, `// then`
- For static Android APIs (e.g. `AppCompatDelegate`), use `mockkStatic(...)` in `@Before`
- Add `fun inject(test: YourTestClass)` to `TestInjector` for each new test class
- **Fake singleton scoping:** `@Binds @Singleton` scopes the parent type (e.g. `LocaleProvider`), not the Fake itself. Injecting `FakeLocaleProvider` directly in a test creates a separate instance from the one Hilt gives to UseCases via `LocaleProvider`. When testing a UseCase that depends on other UseCases that use Fakes, construct the child UseCase manually with the injected Fake (see `GetLanguagesWithCurrentUseCaseTest`)

### What needs tests

- **All UseCases** — every use case gets a test class
- **All ViewModels** — every ViewModel gets a test class
- **Repositories and DataSources** — every repository and local data source gets a test class

### What does NOT need tests

- State/Intent/Effect data classes, Callbacks, Screen composables, DI modules, domain models

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
│   └── values-{locale}/
│       └── strings.xml                     # Translated card names
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
8. Add flavor to CI matrix in `.github/workflows/merge.yml`

### Card Images

Named `img_{name}_whole.jpg` and `img_{name}_half.jpg` (or `_side`/`_leaf` depending on flavor theme), provided at 5 density buckets. Full specs (dimensions, quality, naming): `docs/docs_images.md`, `docs/docs_icons.md`, `docs/docs_logo.md`

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

### Adding translations for a new flavor

For each existing locale, create `app/src/{flavorName}/res/values-{locale}/strings.xml` containing translations of all card names from that flavor's default `values/strings.xml`. Copy the structure from an existing flavor (e.g. `app/src/fruitHalf/res/values-pl/strings.xml`).

## CI/CD

- **PR (`.github/workflows/pull_request.yml`):** Runs unit tests on pull requests (skips for `chore/` commits)
- **Merge to main (`.github/workflows/merge.yml`):** Builds release bundles for all flavors and uploads to Google Play, then auto-bumps version and creates a chore PR

## Code Style

- **No trailing commas** — do NOT add a comma after the last parameter in function declarations, constructor parameters, or function calls. The linter will remove them.
- **No unnecessary blank lines** — do not add blank lines inside function bodies, between consecutive property declarations, or between tightly related statements. One blank line is fine between top-level declarations (functions, classes) and between logical sections, but avoid multiple consecutive blank lines or blank lines that add no readability value.
- **Small, focused functions** — extract logic into small, well-named private functions rather than writing long blocks with deep nesting. Prefer flat, readable code with minimal indentation levels.
- **Every file ends with a single newline** — always ensure exactly one trailing newline at the end of every file.

## Key Conventions

- Kotlin 2.2.21, JVM target 11, Compose enabled — versions managed via `gradle/libs.versions.toml`
- Min SDK 23, Target/Compile SDK 36
- Version format: MAJOR.MINOR.PATCH (versionCode encodes as MMMNNNPPP)
- Product flavors use the "version" dimension; each flavor has its own package name and billing key
