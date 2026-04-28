#!/usr/bin/env python3
"""Generate iOS InfoPlist.strings localizations for the 4 flavor app names.

Reads `app_name_fruit_half`, `app_name_vegetable_half`, `app_name_mammal_side`,
`app_name_bird_side` from every shared/src/commonMain/composeResources/values*/strings.xml
and writes one InfoPlist.strings per (flavor, locale) under
iosApp/iosApp/Localizations/<Flavor>/<locale>.lproj/InfoPlist.strings.

Re-run whenever Android translations change.
"""
from pathlib import Path
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
ANDROID_RES = ROOT / "shared" / "src" / "commonMain" / "composeResources"
IOS_LOCALIZATIONS = ROOT / "iosApp" / "iosApp" / "Localizations"

# Android values dir name -> iOS .lproj name (handle deprecated codes)
ANDROID_TO_IOS_LOCALE = {
    "values": "en",            # Base / default English
    "values-iw": "he",         # Hebrew (Android uses old code)
    "values-in": "id",         # Indonesian (Android uses old code)
    "values-no": "nb",         # Norwegian Bokmål (Android "no" maps to nb)
}

FLAVORS = {
    "FruitHalf": "app_name_fruit_half",
    "VegetableHalf": "app_name_vegetable_half",
    "MammalSide": "app_name_mammal_side",
    "BirdSide": "app_name_bird_side",
}


def android_dir_to_ios_locale(values_dir: str) -> str:
    if values_dir in ANDROID_TO_IOS_LOCALE:
        return ANDROID_TO_IOS_LOCALE[values_dir]
    match = re.match(r"^values-(.+)$", values_dir)
    if not match:
        raise ValueError(f"Unrecognised values dir: {values_dir}")
    return match.group(1)


def read_app_names(strings_xml: Path) -> dict[str, str]:
    tree = ET.parse(strings_xml)
    return {
        node.attrib["name"]: (node.text or "")
        for node in tree.getroot().findall("string")
        if node.attrib.get("name") in set(FLAVORS.values())
    }


def write_infoplist_strings(target_path: Path, display_name: str) -> None:
    target_path.parent.mkdir(parents=True, exist_ok=True)
    # InfoPlist.strings uses UTF-16 LE in Apple defaults but UTF-8 with explicit encoding works too.
    escaped = display_name.replace('\\', '\\\\').replace('"', '\\"')
    target_path.write_text(f'"CFBundleDisplayName" = "{escaped}";\n', encoding="utf-8")


def main() -> None:
    written = 0
    for values_dir in sorted(ANDROID_RES.iterdir()):
        if not values_dir.is_dir() or not values_dir.name.startswith("values"):
            continue
        strings_xml = values_dir / "strings.xml"
        if not strings_xml.exists():
            continue
        names = read_app_names(strings_xml)
        if not names:
            continue
        ios_locale = android_dir_to_ios_locale(values_dir.name)
        for flavor, key in FLAVORS.items():
            display_name = names.get(key)
            if display_name is None:
                continue
            target = IOS_LOCALIZATIONS / flavor / f"{ios_locale}.lproj" / "InfoPlist.strings"
            write_infoplist_strings(target, display_name)
            written += 1
    print(f"Wrote {written} InfoPlist.strings files under {IOS_LOCALIZATIONS}")


if __name__ == "__main__":
    main()
