# 🎴 new app flavor setup

1. Setup new flavor in the app:
    - add the new flavor to the `flavorConfigs` list in `app/build.gradle.kts`
    - add the new flavor package name to the `queries` in `AndroidManifest.xml`

1. Prepare images:
    - prepare card images as described [here](/docs_images)
    - prepare logo images as described [here](/docs_logo)

1. Add all images, strings, colors:
    - copy paste the images to the new flavor folder
    - create the strings in the new flavor folder
    - create the colors in the new flavor folder
    - create a flavor specific `AllCardPairsLocalDataSource.class` in the `data/local/card_pairs`
      folder
    - add the new app in the `AppModel` and include it in the `all` function

1. Add app icon:
    - add prepared app icon by adding a new Image Asset
    - don't trim icon
    - use icon scale 65%
    - use background scale 125%
    - use Circle legacy icon
    - use PNG
    - select proper flavor
    - create a flavor specific `ic_logo.xml` in the `{flavor}/res/drawable` folder

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

1. Push build:
    - change the `minor` version to the `minor + 1` and `patch` to `0`
    - create a new branch
    - commit and Push all the added changes
    - create a Pull Request to the `main` branch
    - merge it
