#!/usr/bin/env python3
"""
Generate Play Store feature graphic (1024x500) for each flavor and locale.

Layout: flavor background, app logo on left, localized label chips on right.
Font auto-sizes for longer translations.

Usage:
    python3 generate_feature_graphic.py <flavor> [locale]
    python3 generate_feature_graphic.py fruit_half          # all locales
    python3 generate_feature_graphic.py fruit_half pl       # single locale
"""

import argparse
import os
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

# ─── Constants ──────────────────────────────────────────────────────

WIDTH, HEIGHT = 1024, 500
CHIP_FONT_SIZE = 28
CHIP_FONT_SIZE_MIN = 20
CHIP_PADDING_H = 24
CHIP_HEIGHT = 52
CHIP_SPACING = 14
CHIP_BG = (255, 255, 255, 160)
CHIP_BORDER = (0, 0, 0, 255)
CHIP_TEXT_COLOR = (50, 50, 50)
CHIP_SS = 3

FLAVOR_COLORS = {
    "fruit_half": (0xFF, 0xEA, 0xA1),
    "vegetable_half": (0xE6, 0xA0, 0xA0),
    "mammal_side": (0xE2, 0xBA, 0x8B),
    "bird_side": (0xB1, 0xDB, 0xE7),
}

FLAVOR_DIR_MAP = {
    "fruit_half": "fruitHalf",
    "vegetable_half": "vegetableHalf",
    "mammal_side": "mammalSide",
    "bird_side": "birdSide",
}

LOCALE_DIR_MAP = {
    "en": "en-US", "ar": "ar", "cs": "cs-CZ", "da": "da-DK", "de": "de-DE",
    "el": "el-GR", "es": "es-ES", "et": "et", "fi": "fi-FI", "fr": "fr-FR",
    "hi": "hi-IN", "hu": "hu-HU", "in": "id", "it": "it-IT", "iw": "iw-IL",
    "ja": "ja-JP", "ko": "ko-KR", "lt": "lt", "lv": "lv", "nl": "nl-NL",
    "no": "no-NO", "pl": "pl-PL", "pt": "pt-PT", "ro": "ro", "ru": "ru-RU",
    "sk": "sk", "sl": "sl", "sv": "sv-SE", "tr": "tr-TR", "uk": "uk",
    "vi": "vi", "zh": "zh-CN",
}

ALL_LOCALES = list(LOCALE_DIR_MAP.keys())

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
FONT_PRIMARY = PROJECT_ROOT / "app/src/main/res/font/patrickhand_regular.ttf"
FONT_FALLBACK = Path("/System/Library/Fonts/Supplemental/Arial Unicode.ttf")
FALLBACK_LOCALES = {"ar", "el", "hi", "iw", "ja", "ko", "ru", "uk", "zh"}

# ─── Localized chip labels ─────────────────────────────────────────

def get_chips(locale):
    chips = {
        "ar": ["مجاني", "بدون إنترنت", "تدريب الدماغ", "تعلّم كلمات جديدة", "30+ لغة"],
        "cs": ["zdarma", "offline", "trénink mozku", "učte se nová slova", "30+ jazyků"],
        "da": ["gratis", "offline", "hjernetræning", "lær nye ord", "30+ sprog"],
        "de": ["kostenlos", "offline", "Gehirntraining", "neue Wörter lernen", "30+ Sprachen"],
        "el": ["δωρεάν", "offline", "εξάσκηση μυαλού", "μάθε νέες λέξεις", "30+ γλώσσες"],
        "es": ["gratis", "sin conexión", "entrena tu mente", "aprende palabras", "30+ idiomas"],
        "et": ["tasuta", "offline", "ajutreening", "õpi uusi sõnu", "30+ keelt"],
        "fi": ["ilmainen", "offline", "aivojumppa", "opi uusia sanoja", "30+ kieltä"],
        "fr": ["gratuit", "hors ligne", "entraînement cérébral", "apprendre des mots", "30+ langues"],
        "hi": ["मुफ़्त", "ऑफ़लाइन", "दिमागी कसरत", "नए शब्द सीखें", "30+ भाषाएँ"],
        "hu": ["ingyenes", "offline", "agytorna", "tanulj új szavakat", "30+ nyelv"],
        "in": ["gratis", "offline", "latih otak", "pelajari kata baru", "30+ bahasa"],
        "it": ["gratuito", "offline", "allena la mente", "impara nuove parole", "30+ lingue"],
        "iw": ["חינם", "אופליין", "אימון מוח", "למד מילים חדשות", "30+ שפות"],
        "ja": ["無料", "オフライン", "脳トレ", "新しい言葉を学ぶ", "30以上の言語"],
        "ko": ["무료", "오프라인", "두뇌 훈련", "새 단어 배우기", "30개 이상 언어"],
        "lt": ["nemokamai", "be interneto", "smegenų treniruotė", "mokykis naujų žodžių", "30+ kalbų"],
        "lv": ["bezmaksas", "bezsaistē", "smadzeņu treniņš", "mācies jaunus vārdus", "30+ valodas"],
        "nl": ["gratis", "offline", "hersentraining", "leer nieuwe woorden", "30+ talen"],
        "no": ["gratis", "offline", "hjernetrening", "lær nye ord", "30+ språk"],
        "pl": ["za darmo", "offline", "trening mózgu", "ucz się nowych słów", "30+ języków"],
        "pt": ["gratuito", "offline", "treino cerebral", "aprenda novas palavras", "30+ idiomas"],
        "ro": ["gratuit", "offline", "antrenament mental", "învață cuvinte noi", "30+ limbi"],
        "ru": ["бесплатно", "офлайн", "тренировка мозга", "учи новые слова", "30+ языков"],
        "sk": ["zadarmo", "offline", "tréning mozgu", "učte sa nové slová", "30+ jazykov"],
        "sl": ["brezplačno", "brez povezave", "trening možganov", "uči se nove besede", "30+ jezikov"],
        "sv": ["gratis", "offline", "hjärnträning", "lär dig nya ord", "30+ språk"],
        "tr": ["ücretsiz", "çevrimdışı", "beyin egzersizi", "yeni kelimeler öğren", "30+ dil"],
        "uk": ["безкоштовно", "офлайн", "тренування мозку", "вчи нові слова", "30+ мов"],
        "vi": ["miễn phí", "ngoại tuyến", "rèn luyện trí não", "học từ mới", "30+ ngôn ngữ"],
        "zh": ["免费", "离线", "脑力训练", "学习新词汇", "30+种语言"],
    }
    return chips.get(locale, ["free", "offline", "brain training", "learn new words", "30+ languages"])


# ─── Helpers ───────────────────────────────────────────────────────

def load_font(locale, size):
    font_path = FONT_FALLBACK if locale in FALLBACK_LOCALES else FONT_PRIMARY
    if font_path.exists():
        return ImageFont.truetype(str(font_path), size)
    return ImageFont.load_default()


def output_dir_for(flavor, locale):
    locale_dir = LOCALE_DIR_MAP.get(locale, locale)
    flavor_dir = FLAVOR_DIR_MAP[flavor]
    return str(
        PROJECT_ROOT / "app/src" / flavor_dir
        / "play/listings" / locale_dir / "graphics/feature-graphic"
    )


# ─── Generator ─────────────────────────────────────────────────────

def generate_feature_graphic(flavor, locale, output_dir):
    bg_color = FLAVOR_COLORS[flavor] + (255,)
    canvas = Image.new("RGBA", (WIDTH, HEIGHT), bg_color)

    # Logo (clean PNG with transparent background)
    logo_name = f"ic_logo_{flavor}.png"
    logo_path = PROJECT_ROOT / "app/src/main/res/drawable" / logo_name

    if logo_path.exists():
        logo = Image.open(str(logo_path)).convert("RGBA")
        max_h = 310
        scale = max_h / logo.height
        logo = logo.resize(
            (int(logo.width * scale), int(logo.height * scale)), Image.LANCZOS
        )
        logo_w = logo.width
    else:
        logo = None
        logo_w = 0

    # Fixed positions — logo on left, chips on right, independent of locale
    logo_x = 66
    chips_right_margin = 50
    chips_column_right = WIDTH - chips_right_margin

    if logo is not None:
        logo_y = (HEIGHT - logo.height) // 2
        canvas.paste(logo, (logo_x, logo_y), logo)

    # Chip labels
    chips = get_chips(locale)
    chips_available_w = chips_column_right - (logo_x + logo_w + 15)

    # Auto-size font
    font_size = CHIP_FONT_SIZE
    while font_size >= CHIP_FONT_SIZE_MIN:
        font = load_font(locale, font_size)
        max_chip_w = max(font.getbbox(t)[2] - font.getbbox(t)[0] + 2 * CHIP_PADDING_H for t in chips)
        if max_chip_w <= chips_available_w:
            break
        font_size -= 1
    else:
        font = load_font(locale, CHIP_FONT_SIZE_MIN)

    draw = ImageDraw.Draw(canvas)

    chip_data = []
    for text in chips:
        bbox = font.getbbox(text)
        cw = bbox[2] - bbox[0] + 2 * CHIP_PADDING_H
        chip_data.append((text, cw))

    ch = CHIP_HEIGHT
    total_h = len(chip_data) * ch + (len(chip_data) - 1) * CHIP_SPACING
    start_y = (HEIGHT - total_h) // 2

    # Left-align chips at fixed position
    chips_left_x = logo_x + logo_w + 30

    for idx, (text, cw) in enumerate(chip_data):
        x = chips_left_x
        y = start_y + idx * (ch + CHIP_SPACING)
        radius = ch // 2

        # Anti-aliased pill via supersampling
        ss = CHIP_SS
        chip_ss = Image.new("RGBA", (cw * ss, ch * ss), (0, 0, 0, 0))
        ImageDraw.Draw(chip_ss).rounded_rectangle(
            [0, 0, cw * ss - 1, ch * ss - 1],
            radius=radius * ss, fill=CHIP_BG,
            outline=CHIP_BORDER, width=2 * ss
        )
        chip_img = chip_ss.resize((cw, ch), Image.LANCZOS)
        canvas.paste(chip_img, (x, y), chip_img)

        draw.text((x + cw // 2, y + ch // 2), text, fill=CHIP_TEXT_COLOR, font=font, anchor="mm")

    os.makedirs(output_dir, exist_ok=True)
    out_path = Path(output_dir) / "1.png"
    canvas.convert("RGB").save(str(out_path), "PNG")


# ─── Main ──────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="Generate Play Store feature graphic")
    parser.add_argument("flavor", choices=FLAVOR_COLORS.keys())
    parser.add_argument("locale", nargs="?", default=None, help="Locale code (default: all)")
    args = parser.parse_args()

    locales = [args.locale] if args.locale else ALL_LOCALES

    for locale in locales:
        out = output_dir_for(args.flavor, locale)
        generate_feature_graphic(args.flavor, locale, out)

    print(f"  [{args.flavor}] Feature graphics generated for {len(locales)} locale(s)")


if __name__ == "__main__":
    main()
