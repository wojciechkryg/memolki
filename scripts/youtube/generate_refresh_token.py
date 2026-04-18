#!/usr/bin/env python3
"""
Generates an OAuth2 refresh token for the Google Ads API.

Uses the same client_secret.json as the YouTube upload script but requests
Google Ads scopes. The refresh token is printed to the console — copy it
into google-ads.yaml.

Usage:
    python3 scripts/youtube/generate_refresh_token.py
"""

import sys
from pathlib import Path

from google_auth_oauthlib.flow import InstalledAppFlow

SCRIPT_DIR = Path(__file__).resolve().parent
CLIENT_SECRET_FILE = SCRIPT_DIR / "client_secret.json"

SCOPES = ["https://www.googleapis.com/auth/adwords"]


def main():
    if not CLIENT_SECRET_FILE.exists():
        print(f"❌ Missing {CLIENT_SECRET_FILE}")
        sys.exit(1)

    print("🔑 Opening browser for Google Ads OAuth consent...")
    print("   Grant access to your Google Ads account.\n")

    flow = InstalledAppFlow.from_client_secrets_file(str(CLIENT_SECRET_FILE), SCOPES)
    creds = flow.run_local_server(port=0, prompt="consent")

    if not creds.refresh_token:
        print("❌ No refresh_token returned by Google.")
        print("   Revoke the app at https://myaccount.google.com/permissions and retry.")
        sys.exit(1)

    print("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    print("✅ Refresh token generated!\n")
    print(f"   {creds.refresh_token}\n")
    print("Copy this into google-ads.yaml under 'refresh_token:'")
    print("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")


if __name__ == "__main__":
    main()
