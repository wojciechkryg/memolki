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

Add the `google-services.json` file in the `app/` directory (download from Firebase console).
