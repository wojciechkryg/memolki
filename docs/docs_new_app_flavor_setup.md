# 🎴 new app flavor setup

1. Setup new flavor in the app:
    - Go to file `app/build.gradle.kts`
    - Add the new flavor to the `productFlavors` by copy paste the other flavor
    - In the `create` change to `{flavor name}`
    - In the `applicationIdSuffix` change to `.{flavor name}`

1. Setup new flavor in the CI:
    - Go to file `.github/workflows/merge.yml`
    - Add the new flavor to the `flavor` matrix

1. Setup new flavor in the Google Play Console:
    - Go to https://play.google.com/console/u/0/developers/9083635429558058910/create-new-app
    - Enter app name `memolki • {name connected to flavor}`
    - Choose `Game`
    - Choose `Paid`
    - Check all declarations
    - Click `Create`

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
    -Merge it
