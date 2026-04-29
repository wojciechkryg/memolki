# 🎴 new app flavor setup

1. Setup new flavor in the app:
    - add the new flavor to the `flavorConfigs` list in `androidApp/build.gradle.kts`
    - add the new flavor to the `productFlavors` block in `shared/build.gradle.kts` (and to the `flavorNames` list in the `afterEvaluate` block — this wires the per-flavor source set into `iosMain` when active)
    - add the new flavor package name to the `queries` in `AndroidManifest.xml`

1. Prepare images:
    - prepare card images as described [here](./docs_images.md)
    - prepare logo images as described [here](./docs_logo.md)

1. Add all images, strings, colors:
    - drop card images into `shared/src/{flavor}Main/composeResources/drawable/` (single source for both Android and iOS via Compose Resources)
    - drop card name strings into `shared/src/{flavor}Main/composeResources/values{-locale}/strings.xml`
    - create the per-flavor primary color in `shared/src/{flavor}Main/kotlin/.../ui/theme/AppPrimary.kt` (`internal actual val AppPrimary: Color = ...`)
    - create a flavor-specific `CardCatalog.kt` in `shared/src/{flavor}Main/kotlin/com/wojdor/memolki/data/local/card/` that implements `AllCardPairsDataSource`
    - copy Android-specific launcher/notification icons into `androidApp/src/{flavor}/res/`
    - add the new app in `AppModel` (commonMain) and include it in the `all` function

1. Add app icon:
    - add prepared app icon by adding a new Image Asset
    - don't trim icon
    - use icon scale 65%
    - use background scale 125%
    - use Circle legacy icon
    - use PNG
    - select proper flavor
    - create a flavor specific `ic_logo.xml` in the `{flavor}/res/drawable` folder

1. Add app shortcuts:
    - create `{flavor}/res/xml/shortcuts.xml` by copying from an existing flavor
    - update `android:targetPackage` to the new flavor's application ID (e.g. `com.wojdor.memolki.newflavor`)

1. Setup new flavor in the Google Play Console:
    - go to https://play.google.com/console/u/0/developers/9083635429558058910/create-new-app
    - enter app name `memolki • {name connected to flavor}`
    - choose `Game`
    - choose `Free`
    - check all declarations
    - click `Create`

1. go through the configuration in the Google Play Console

1. Setup Google Play Games:
    - go
      to https://play.google.com/console/u/0/developers/9083635429558058910/app/4974380132955156236/games/leaderboards
    - create a new project in Google Cloud named "memolki - {name connected to flavor}"
    - create a OAuth consent screen
    - create OAuth login details
    - create "Total Coins" and "Total Card Pairs Matched"
    - use icon images from [here](../images)
    - copy identificators to the flavour's `strings_non_translatable.xml`

1. Setup AdMob:
    - go to https://admob.google.com/v2/apps/list
    - add app
    - select platform `Android`
    - select `No` for app availability on Google Play
    - enter app name `memolki • {flavor name}`
    - click `Add`
    - go to `App settings`
    - copy `App ID` (looks like `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`) and add it to the flavor's
      `res/values/ad_mob_ids.xml` as `ad_mob_app_id`
    - create `Rewarded ad`s with the same names and settings as
      in https://admob.google.com/v2/apps/8308414287/adunits/list
    - copy `Ad Unit ID`s (looks like `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`) and add them to the
      flavor's `res/values/ad_mob_ids.xml`.

1. Setup App Content in Google Play Console:
    - go to the new app's `App content` section in Google Play Console
    - copy the **Data safety** form from an existing flavor (same ad SDKs, same analytics, same data collection)
    - copy the **Ads** declaration (contains ads)
    - copy the **Target audience** settings (children-directed / COPPA)
    - copy the **Privacy policy** URL
    - copy the **Content rating** questionnaire answers

1. Update Firebase:
    - go to [Firebase Console](https://console.firebase.google.com) → project → Project settings → Add app
    - add the new flavor's Android package name (e.g. `com.wojdor.memolki.newflavor`)
    - download the updated `google-services.json` and replace `androidApp/google-services.json`
    - re-encode and update the `GOOGLE_SERVICES_JSON` GitHub secret:
      ```bash
      base64 -i androidApp/google-services.json | pbcopy
      ```
    - go to GitHub → Settings → Secrets and variables → Actions → update `GOOGLE_SERVICES_JSON`

1. Update mediation partners:
    - go to https://admob.google.com/v2/mediation/groups/4675702647/edit
    - add the new app in all AdMob mediation partners that require per-app configuration

1. Setup new flavor in the CI:
    - go to file `.github/workflows/merge.yml`
    - add the new flavor to the `flavor` matrix
    - add flavor's billing key to the `Build with Gradle` step
    - go
      to https://play.google.com/console/u/0/developers/9083635429558058910/app/4974380132955156236/monetization-setup
    - copy the public RSA billing key
    - add it to the local `/secrets.properties` file
    - go to GitHub to Settings/Secrets and variables/Actions
    - add the public RSA billing key

1. Setup pushing builds from CI to Google Play Console :
    - go
      to https://play.google.com/console/u/0/developers/9083635429558058910/users-and-permissions/google-play-store-deploy%40memolki.iam.gserviceaccount.com
    - click `Add application`
    - choose new application
    - go to `Version` section and check the `Creation of production builds`
    - click `Publish`

1. Generate Play Store listing assets:
    - add translations for the new flavor's screenshot texts in `compose_screenshots.py` → `get_texts()`
    - add translations for the new flavor's feature graphic chips in `generate_feature_graphic.py` → `get_chips()`
    - add the flavor's background color to `FLAVOR_COLORS` in both scripts
    - generate screenshots: `./scripts/screenshot/generate_all_screenshots.sh {flavor_name}`
    - generate feature graphics: `./scripts/screenshot/generate_feature_graphics.sh {flavor_name}`
    - see [docs_screenshot.md](docs_screenshot.md) for details

1. Push listings to Play Console:
    - push all listing assets (titles, descriptions, screenshots, feature graphics):
      ```bash
      ./scripts/listing/update_listings.sh {flavorCamelCase}
      ```
    - see [docs_listing.md](docs_listing.md) for details

1. Setup the new flavor on iOS:
    - add a new entry to `iosApp/Configs/{Flavor}.xcconfig` with `PRODUCT_BUNDLE_IDENTIFIER = com.wojdor.memolki.{flavor}` and the matching display name
    - add a new target/scheme entry in `iosApp/project.yml` mirroring the existing flavors (config + scheme block with the pre-action that writes the camelCase flavor name to `iosApp/.active-flavor`)
    - regenerate the Xcode project: `cd iosApp && xcodegen generate`
    - register the iOS bundle in the Firebase Console (same project as Android), download `GoogleService-Info.plist`, and save it as `iosApp/iosApp/Resources/GoogleService-Info-{Flavor}.plist`
    - add the per-flavor `AppIcon-{Flavor}.appiconset` to `iosApp/iosApp/Assets.xcassets/`
    - localize the display name in `iosApp/iosApp/Resources/{lang}.lproj/InfoPlist.strings` for each supported language
    - run `pod install` and verify the new scheme builds: `xcodebuild -workspace iosApp.xcworkspace -scheme iosApp-{Flavor} -sdk iphonesimulator -destination "platform=iOS Simulator,name=iPhone 17 Pro" CODE_SIGNING_ALLOWED=NO build`

1. Push build:
    - change the `minor` version to the `minor + 1` and `patch` to `0`
    - create a new branch
    - commit and Push all the added changes
    - create a Pull Request to the `main` branch
    - merge it
