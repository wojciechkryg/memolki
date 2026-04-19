# Play Store Listing Management

Fetch and update Play Store listings (title, short description, full description) for all flavors using the [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher) plugin.

## Prerequisites

- **Google Play service account JSON** with Play Console permissions
- `PLAY_SERVICE_ACCOUNT_PATH` set in `secrets.properties`

### Getting the service account key

1. List service accounts:
   ```bash
   gcloud iam service-accounts list --project=memolki-wla
   ```

2. Download the key:
   ```bash
   gcloud iam service-accounts keys create play-service-account.json \
       --iam-account=<SERVICE_ACCOUNT_EMAIL> \
       --project=memolki-wla
   ```

3. Move it somewhere safe outside the repo (e.g. `~/.config/memolki/`) and add the path to `secrets.properties`:
   ```
   PLAY_SERVICE_ACCOUNT_PATH=/Users/wojciech/.config/memolki/play-service-account.json
   ```

> The file pattern `*-service-account*.json` is gitignored, but storing it outside the repo is safer.

## Usage

### Fetch current listings

Downloads all existing titles and descriptions from Play Store:

```bash
./scripts/listing/fetch_listings.sh
```

Creates files at `androidApp/src/{flavor}/play/listings/{locale}/`:

```
androidApp/src/fruitHalf/play/listings/
├── en-US/
│   ├── title.txt
│   ├── short-description.txt
│   ├── full-description.txt
│   └── video-url.txt
├── pl-PL/
│   ├── title.txt
│   ├── short-description.txt
│   ├── full-description.txt
│   └── video-url.txt
└── ...
```

### Update listings

Edit the text files locally, then push to Play Store:

```bash
./scripts/listing/update_listings.sh                # All flavors
./scripts/listing/update_listings.sh fruitHalf       # Specific flavor
```

## Flavors

| Flavor | Package name |
|--------|-------------|
| `fruitHalf` | `com.wojdor.memolki.fruithalf` |
| `vegetableHalf` | `com.wojdor.memolki.vegetablehalf` |
| `mammalSide` | `com.wojdor.memolki.mammalside` |
| `birdSide` | `com.wojdor.memolki.birdside` |

## File constraints (Play Store limits)

| File | Max length |
|------|-----------|
| `title.txt` | 30 characters |
| `short-description.txt` | 80 characters |
| `full-description.txt` | 4000 characters |
| `video-url.txt` | YouTube URL (no character limit) |
