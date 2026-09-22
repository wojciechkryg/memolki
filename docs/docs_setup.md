# 🔧 setup

## `secrets.properties`

Add the `secrets.properties` file in the root directory with the following content:

```
# Billing keys (one per flavor)
FRUIT_HALF_BILLING_KEY="<billing_key>"
VEGETABLE_HALF_BILLING_KEY="<billing_key>"
MAMMAL_SIDE_BILLING_KEY="<billing_key>"
BIRD_SIDE_BILLING_KEY="<billing_key>"

# Firebase
FIREBASE_PROJECT_ID="<firebase_project_id>"

# Google Play publishing
PLAY_SERVICE_ACCOUNT_PATH=/path/to/play-service-account.json
```

## `google-services.json`

Add the `google-services.json` file in the `androidApp/` directory (download from Firebase console).

## iOS toolchain

Required to build the iOS app:

- **Xcode 15+** (current Compose Multiplatform target is iOS 15)
- **xcodegen** — generates `iosApp.xcodeproj` from `iosApp/project.yml`:
  ```bash
  brew install xcodegen
  ```
- **CocoaPods** — installed pods are wired through Kotlin's `native.cocoapods` plugin:
  ```bash
  brew install cocoapods
  ```

### `GoogleService-Info.plist` (per flavor)

Each iOS flavor needs its own Firebase plist downloaded from the Firebase Console for the matching bundle ID (`com.wojdor.memolki.{flavor}`). Place them in:

```
iosApp/iosApp/Resources/GoogleService-Info-FruitHalf.plist
iosApp/iosApp/Resources/GoogleService-Info-VegetableHalf.plist
iosApp/iosApp/Resources/GoogleService-Info-MammalSide.plist
iosApp/iosApp/Resources/GoogleService-Info-BirdSide.plist
```

A "Copy Firebase plist for active flavor" post-build script (defined in `project.yml`, so it survives `xcodegen generate`) copies the target's plist into the app bundle as `GoogleService-Info.plist` so `FirebaseApp.configure()` finds it. If the plist is missing the build succeeds with a warning and Firebase stays disabled (the guard in `iosAppApp.swift`).

### Generate the Xcode project

```bash
cd iosApp
xcodegen generate     # produces iosApp.xcodeproj from project.yml
pod install           # also runs :shared:syncFramework via the Kotlin cocoapods plugin
open iosApp.xcworkspace
```

Pick one of the four schemes (`iosApp-FruitHalf`, `iosApp-VegetableHalf`, `iosApp-MammalSide`, `iosApp-BirdSide`) before running. Each scheme's pre-action writes its flavor name to `iosApp/.active-flavor`, which `shared/build.gradle.kts` reads to wire the per-flavor source set into `iosMain`.
