# 🎴 new app flavor setup

1. Prepare AdMob setup:
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
