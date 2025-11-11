# 🎴 new app flavor setup

1. Setup new flavor in the app:
    - Go to file `app/build.gradle.kts`
    - Add the new flavor to the `flavorConfigs` list

1. Setup new flavor in the CI:
    - Go to file `.github/workflows/merge.yml`
    - Add the new flavor to the `flavor` matrix

1. Add app icon:
    - add prepared app icon by adding a new Image Asset
    - don't trim icon
    - use icon scale 65%
    - use background scale 125%
    - use Circle legacy icon
    - use png
    - select proper flavor

1. Add all images, strings, colors:
    - copy paste the images to the new flavor folder
    - create the strings in the new flavor folder
    - create the colors in the new flavor folder
    - create a flavor specific `AllCardPairsLocalDataSource.class` in the `data/local/card_pairs` folder

1. Setup new flavor in the Google Play Console:
    - Go to https://play.google.com/console/u/0/developers/9083635429558058910/create-new-app
    - Enter app name `memolki • {name connected to flavor}`
    - Choose `Game`
    - Choose `Paid`
    - Check all declarations
    - Click `Create`

1. Go through the configuration in the Google Play Console

1. Setup Google Play Games:
   - Go to https://play.google.com/console/u/0/developers/9083635429558058910/app/4974380132955156236/games/leaderboards
   - Create a new project in Google Cloud named "memolki - {name connected to flavor}"
   - Create a OAuth consent screen
   - Create OAuth login details
   - Create "Total Coins" and "Total Card Pairs Matched"
   - Use icon images from [here](../images)
   - Copy identificators to the flavour's `strings_non_translatable.xml`

1. Setup AdMob:
    - Go to https://admob.google.com/v2/apps/list
    - Add app
    - Select platform `Android`
    - Select `No` for app availability on Google Play
    - Enter app name `memolki • {flavor name}`
    - Click `Add`
    - Go to `App settings`
    - Copy `App ID` (looks like `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`) and add it to the flavor's `res/values/ad_mob_ids.xml` as `ad_mob_app_id`
    - Create `Rewarded ad`s with the same names and settings as in https://admob.google.com/v2/apps/8308414287/adunits/list
    - Copy `Ad Unit ID`s (looks like `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`) and add them to the flavor's `res/values/ad_mob_ids.xml`.

1. Setup pushing builds from CI to Google Play Console :
    - Go to https://play.google.com/console/u/0/developers/9083635429558058910/users-and-permissions/google-play-store-deploy%40memolki.iam.gserviceaccount.com
    - Click `Add application`
    - Choose new application
    - Go to `Version` section and check the `Creation of production builds`
    - Click `Publish`

1. Push build:
    - Change the `minor` version to the `minor + 1` and `patch` to `0`
    - Create a new branch
    - Commit and Push all the added changes
    - Create a Pull Request to the `main` branch
    - Merge it
