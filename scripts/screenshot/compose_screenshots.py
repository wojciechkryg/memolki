#!/usr/bin/env python3
"""
Compose Play Store listing screenshots from raw emulator captures.

Renders each screenshot with a device mockup (anti-aliased, rotated) on a
colored background with localized text overlay. Uses a wave layout: devices
alternate between top and bottom positions with text in the opposite area.
Neighboring device edges peek from behind via per-screenshot layering.

Usage:
    python3 compose_screenshots.py <flavor> <locale> <raw_dir> [--output-dir path]

Example:
    python3 compose_screenshots.py fruit_half en /tmp/memolki_raw
    python3 compose_screenshots.py mammal_side pl /tmp/memolki_raw --output-dir ~/Desktop/test
"""

import argparse
import math
import os
from pathlib import Path

from PIL import Image, ImageChops, ImageDraw, ImageFont

# ─── Constants ──────────────────────────────────────────────────────

CANVAS_W, CANVAS_H = 1080, 1920
NUM_SCREENSHOTS = 5
BORDER = 40
CORNER_OUTER = 200
CORNER_INNER = 165
ROTATION_DEG = 7
TEXT_SIZE = 100
TEXT_LINE_SPACING = 14
DEVICE_WIDTH_RATIO = 0.65

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

# Wave layout: alternating top/bottom device positions.
# "upper" = device at top, text below.  "lower" = device at bottom, text above.
SCREENSHOT_LAYOUT = [
    ("upper", False),  # 1: 3×4 Gameplay — text top, device bottom
    ("lower", True),   # 2: Collection (top) — device top, text bottom
    ("upper", False),  # 3: 5×6 Gameplay — text top, device bottom
    ("lower", True),   # 4: Daily Challenge End — device top, text bottom
    ("upper", False),  # 5: Collection (locked) — text top, device bottom
]

SCRIPT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent
FONT_PRIMARY = PROJECT_ROOT / "app/src/main/res/font/patrickhand_regular.ttf"
FONT_FALLBACK = Path("/System/Library/Fonts/Supplemental/Arial Unicode.ttf")
FALLBACK_LOCALES = {"ar", "el", "hi", "iw", "ja", "ko", "ru", "uk", "zh"}


# ─── Localized texts ───────────────────────────────────────────────

def get_texts(locale):
    """Return 5 (line1, line2) tuples for the screenshot overlay texts."""
    texts = {
        "ar": [("ليست لعبة ذاكرة", "عادية!"), ("كم بطاقة يمكنك", "فتحها؟"), ("هل تستطيع", "التعامل مع هذا؟"), ("مستعد لأحجية", "اليوم؟"), ("اجمعها", "كلها!")],
        "cs": [("tohle není běžná", "paměťová hra!"), ("kolik jich", "odemkneš?"), ("zvládneš", "tohle?"), ("připraven na dnešní", "hádanku?"), ("posbírej je", "všechny!")],
        "da": [("ikke dit sædvanlige", "hukommelsesspil!"), ("hvor mange kan", "du låse op?"), ("tror du, du kan", "klare det?"), ("klar til dagens", "puslespil?"), ("saml dem", "alle!")],
        "de": [("kein gewöhnliches", "gedächtnisspiel!"), ("wie viele kannst", "du freischalten?"), ("schaffst du", "das hier?"), ("bereit für das", "tagesrätsel?"), ("sammle sie", "alle!")],
        "el": [("όχι το συνηθισμένο", "παιχνίδι μνήμης!"), ("πόσα μπορείς", "να ξεκλειδώσεις;"), ("νομίζεις ότι", "τα καταφέρνεις;"), ("έτοιμος για το", "σημερινό παζλ;"), ("συλλέξτε τα", "όλα!")],
        "es": [("¡no es el típico", "juego de memoria!"), ("¿cuántas puedes", "desbloquear?"), ("¿crees que puedes", "con esto?"), ("¿listo para el", "puzzle de hoy?"), ("¡colecciónalos", "todos!")],
        "et": [("see pole tavaline", "mälumäng!"), ("kui palju suudad", "avada?"), ("arvad, et", "saad hakkama?"), ("valmis tänaseks", "pussleks?"), ("kogu need", "kõik!")],
        "fi": [("ei tavallinen", "muistipeli!"), ("kuinka monta", "avaat?"), ("luuletko", "pärjääväsi?"), ("valmis päivän", "pulmaan?"), ("kerää ne", "kaikki!")],
        "fr": [("pas un jeu de", "mémoire ordinaire !"), ("combien pouvez-vous", "débloquer ?"), ("pensez-vous y", "arriver ?"), ("prêt pour le", "puzzle du jour ?"), ("collectionnez-les", "tous !")],
        "hi": [("यह सामान्य मेमोरी", "गेम नहीं है!"), ("कितने अनलॉक", "कर सकते हैं?"), ("क्या आप इसे", "संभाल सकते हैं?"), ("आज की पहेली के", "लिए तैयार?"), ("सभी को", "इकट्ठा करो!")],
        "hu": [("nem a szokásos", "memóriajáték!"), ("hányat tudsz", "feloldani?"), ("szerinted", "megbirkózol vele?"), ("készen állsz a", "mai feladványra?"), ("gyűjtsd össze", "mindet!")],
        "in": [("bukan permainan", "memori biasa!"), ("berapa banyak yang", "bisa kamu buka?"), ("menurutmu bisa", "mengatasinya?"), ("siap untuk puzzle", "hari ini?"), ("kumpulkan", "semuanya!")],
        "it": [("non il solito", "gioco di memoria!"), ("quante riesci a", "sbloccare?"), ("pensi di farcela", "con questo?"), ("pronto per il", "puzzle di oggi?"), ("collezionali", "tutti!")],
        "iw": [("לא משחק זיכרון", "רגיל!"), ("כמה תצליח", "לפתוח?"), ("חושב שתצליח", "עם זה?"), ("מוכן לחידה", "של היום?"), ("אסוף את", "כולם!")],
        "ja": [("普通の神経衰弱", "じゃない！"), ("いくつ", "開けられる？"), ("これに", "挑戦できる？"), ("今日のパズルに", "挑戦！"), ("全部", "集めよう！")],
        "ko": [("평범한 메모리 게임이", "아닙니다!"), ("몇 개나", "잠금 해제할 수 있나요?"), ("이걸 해낼 수", "있다고 생각해?"), ("오늘의 퍼즐", "준비됐나요?"), ("모두", "수집하세요!")],
        "lt": [("ne įprastas", "atminties žaidimas!"), ("kiek gali", "atrakinti?"), ("manai, kad", "susidorosi?"), ("pasiruošęs šiandienos", "galvosūkiui?"), ("surink", "visus!")],
        "lv": [("ne parasta", "atmiņas spēle!"), ("cik daudz vari", "atslēgt?"), ("domā, ka", "tiksi galā?"), ("gatavs šodienas", "mīklai?"), ("savāc", "visas!")],
        "nl": [("geen gewoon", "geheugenspel!"), ("hoeveel kun je", "ontgrendelen?"), ("denk je dit", "aan te kunnen?"), ("klaar voor de", "puzzel van vandaag?"), ("verzamel ze", "allemaal!")],
        "no": [("ikke et vanlig", "hukommelsesspill!"), ("hvor mange kan", "du låse opp?"), ("tror du at du", "klarer dette?"), ("klar for dagens", "puslespill?"), ("samle dem", "alle!")],
        "pl": [("to nie zwykła gra", "pamięciowa!"), ("ile zdołasz", "odblokować?"), ("myślisz, że", "dasz radę?"), ("gotowy na dzisiejszą", "łamigłówkę?"), ("zbierz je", "wszystkie!")],
        "pt": [("não é um jogo de", "memória comum!"), ("quantas consegues", "desbloquear?"), ("achas que", "consegues?"), ("pronto para o", "puzzle de hoje?"), ("coleciona-os", "todos!")],
        "ro": [("nu e un joc de", "memorie obișnuit!"), ("câte poți", "debloca?"), ("crezi că poți", "face față?"), ("gata pentru puzzle-ul", "de azi?"), ("colectează-le", "pe toate!")],
        "ru": [("это не обычная", "игра на память!"), ("сколько сможешь", "открыть?"), ("думаешь,", "справишься?"), ("готов к сегодняшней", "головоломке?"), ("собери их", "все!")],
        "sk": [("nie je to bežná", "pamäťová hra!"), ("koľko dokážeš", "odomknúť?"), ("myslíš, že to", "zvládneš?"), ("pripravený na dnešnú", "hádanku?"), ("pozbieraj ich", "všetky!")],
        "sl": [("to ni običajna", "igra spomina!"), ("koliko jih lahko", "odklenеš?"), ("misliš, da", "zmoreš?"), ("pripravljen na", "današnjo uganko?"), ("zberi jih", "vse!")],
        "sv": [("inte ett vanligt", "minnesspel!"), ("hur många kan", "du låsa upp?"), ("tror du att du", "klarar det?"), ("redo för dagens", "pussel?"), ("samla alla", "kort!")],
        "tr": [("sıradan bir hafıza", "oyunu değil!"), ("kaç tanesini", "açabilirsin?"), ("bununla başa", "çıkabilir misin?"), ("bugünkü bulmacaya", "hazır mısın?"), ("hepsini", "topla!")],
        "uk": [("це не звичайна", "гра на пам'ять!"), ("скільки зможеш", "відкрити?"), ("думаєш,", "впораєшся?"), ("готовий до", "сьогоднішньої головоломки?"), ("зібери їх", "усіх!")],
        "vi": [("không phải trò chơi", "trí nhớ thông thường!"), ("bạn mở khóa", "được bao nhiêu?"), ("bạn nghĩ mình", "xử lý được?"), ("sẵn sàng cho câu đố", "hôm nay?"), ("sưu tầm tất", "cả!")],
        "zh": [("不是普通的", "记忆游戏！"), ("你能解锁", "多少？"), ("你能", "应对吗？"), ("准备好今天的", "谜题了吗？"), ("收集", "全部！")],
    }
    return texts.get(locale, [
        ("not your usual", "memory game!"),
        ("how many can", "you unlock?"),
        ("think you can", "handle this?"),
        ("ready for today's", "puzzle?"),
        ("collect them", "all!"),
    ])


# ─── Device frame ──────────────────────────────────────────────────

def create_device_frame(screenshot_path):
    """Create a device mockup with anti-aliased edges via 4x supersampling."""
    screenshot = Image.open(screenshot_path).convert("RGBA")
    sw, sh = screenshot.size

    SS = 4
    border_ss = BORDER * SS
    corner_outer_ss = CORNER_OUTER * SS
    corner_inner_ss = CORNER_INNER * SS
    bw_ss = sw * SS + 2 * border_ss
    bh_ss = sh * SS + 2 * border_ss

    outer_mask = Image.new("L", (bw_ss, bh_ss), 0)
    ImageDraw.Draw(outer_mask).rounded_rectangle(
        [0, 0, bw_ss - 1, bh_ss - 1], radius=corner_outer_ss, fill=255
    )

    inner_mask = Image.new("L", (bw_ss, bh_ss), 0)
    ImageDraw.Draw(inner_mask).rounded_rectangle(
        [border_ss, border_ss,
         border_ss + sw * SS - 1, border_ss + sh * SS - 1],
        radius=corner_inner_ss, fill=255,
    )

    screen_mask_ss = Image.new("L", (sw * SS, sh * SS), 0)
    ImageDraw.Draw(screen_mask_ss).rounded_rectangle(
        [0, 0, sw * SS - 1, sh * SS - 1], radius=corner_inner_ss, fill=255
    )

    frame_mask = ImageChops.subtract(outer_mask, inner_mask)

    screenshot_ss = screenshot.resize((sw * SS, sh * SS), Image.LANCZOS)
    result_ss = Image.new("RGBA", (bw_ss, bh_ss), (0, 0, 0, 0))
    result_ss.paste(screenshot_ss, (border_ss, border_ss), screen_mask_ss)
    black = Image.new("RGBA", (bw_ss, bh_ss), (0, 0, 0, 255))
    result_ss.paste(black, (0, 0), frame_mask)

    bw = sw + 2 * BORDER
    bh = sh + 2 * BORDER
    return result_ss.resize((bw, bh), Image.LANCZOS)


# ─── Per-screenshot compositing ───────────────────────────────────

def _make_device(raw_path):
    """Create, scale, and rotate a device frame with 2x rotation supersampling."""
    device = create_device_frame(str(raw_path))
    target_w = int(CANVAS_W * DEVICE_WIDTH_RATIO)
    scale = target_w / device.width
    device = device.resize(
        (int(device.width * scale), int(device.height * scale)), Image.LANCZOS
    )
    double = device.resize((device.width * 2, device.height * 2), Image.LANCZOS)
    double = double.rotate(ROTATION_DEG, resample=Image.BICUBIC, expand=True)
    return double.resize((double.width // 2, double.height // 2), Image.LANCZOS)


def _device_dy(position, device_height):
    """Vertical offset: 'lower' = device at top, 'upper' = device at bottom."""
    margin = int(CANVAS_H * 0.02)
    if position == "lower":
        return margin
    else:
        return CANVAS_H - margin - device_height


def _device_h_offset(position, device_height):
    """Horizontal offset to equalize visual gaps between alternating positions.
    Compensates for the rotation-induced visual shift based on vertical position."""
    dy = _device_dy(position, device_height)
    cy = dy + device_height / 2
    return int((cy - CANVAS_H / 2) * math.tan(math.radians(ROTATION_DEG)))


def compose_all(raw_dir, flavor, locale, output_dir):
    """Render each screenshot: main device on top, neighbor edges behind."""
    bg_color = FLAVOR_COLORS[flavor] + (255,)
    all_texts = get_texts(locale)
    font = load_font(locale)
    os.makedirs(output_dir, exist_ok=True)

    devices = []
    for i in range(NUM_SCREENSHOTS):
        raw_path = Path(raw_dir) / f"raw_{i + 1}.png"
        if raw_path.exists():
            devices.append(_make_device(raw_path))
        else:
            print(f"  Warning: {raw_path} not found")
            devices.append(None)

    for i in range(NUM_SCREENSHOTS):
        if devices[i] is None:
            continue

        canvas = Image.new("RGBA", (CANVAS_W, CANVAS_H), bg_color)
        position, _ = SCREENSHOT_LAYOUT[i]
        line1, line2 = all_texts[i]

        h_offset = _device_h_offset(position, devices[i].height)
        main_dx = CANVAS_W // 2 - devices[i].width // 2 + h_offset
        main_dy = _device_dy(position, devices[i].height)

        # Neighbor devices behind main
        for j in [i - 1, i + 1]:
            if 0 <= j < NUM_SCREENSHOTS and devices[j] is not None:
                nb_pos = SCREENSHOT_LAYOUT[j][0]
                nb_h_offset = _device_h_offset(nb_pos, devices[j].height)
                nb_dx = (CANVAS_W // 2 - devices[j].width // 2) + (j - i) * CANVAS_W + nb_h_offset
                nb_dy = _device_dy(nb_pos, devices[j].height)
                canvas.paste(devices[j], (nb_dx, nb_dy), devices[j])

        # Main device on top
        canvas.paste(devices[i], (main_dx, main_dy), devices[i])

        # Text area
        pad = 30
        if position == "lower":
            text_area_top = main_dy + devices[i].height + pad
            text_area_bottom = CANVAS_H - 120
        else:
            text_area_top = pad
            text_area_bottom = main_dy - pad

        # Horizontal centered text
        draw = ImageDraw.Draw(canvas)
        bbox1 = font.getbbox(line1)
        bbox2 = font.getbbox(line2)
        line1_w, line1_h = bbox1[2] - bbox1[0], bbox1[3] - bbox1[1]
        line2_w, line2_h = bbox2[2] - bbox2[0], bbox2[3] - bbox2[1]
        total_text_h = line1_h + TEXT_LINE_SPACING + line2_h

        text_area_h = text_area_bottom - text_area_top
        text_y = text_area_top + (text_area_h - total_text_h) // 2
        draw.text(((CANVAS_W - line1_w) // 2, text_y), line1, fill=(0, 0, 0), font=font)
        draw.text(((CANVAS_W - line2_w) // 2, text_y + line1_h + TEXT_LINE_SPACING), line2, fill=(0, 0, 0), font=font)

        out_path = Path(output_dir) / f"{i + 1}.jpg"
        canvas.convert("RGB").save(str(out_path), "JPEG", quality=95)
        print(f"  [{flavor}/{locale}] Screenshot {i + 1} → {out_path}")

    print(f"  [{flavor}/{locale}] Done!")


def load_font(locale):
    """Load the appropriate font for the locale."""
    font_path = FONT_FALLBACK if locale in FALLBACK_LOCALES else FONT_PRIMARY
    if not font_path.exists():
        print(f"Warning: Font not found: {font_path}, using default")
        return ImageFont.load_default()
    return ImageFont.truetype(str(font_path), TEXT_SIZE)


# ─── Main ──────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="Compose Play Store screenshots")
    parser.add_argument("flavor", choices=FLAVOR_COLORS.keys())
    parser.add_argument("locale", help="Locale code (e.g., en, pl, ja)")
    parser.add_argument("raw_dir", help="Directory with raw_1.png through raw_5.png")
    parser.add_argument("--output-dir", help="Override output directory")
    args = parser.parse_args()

    if args.output_dir:
        output_dir = args.output_dir
    else:
        locale_dir = LOCALE_DIR_MAP.get(args.locale, args.locale)
        flavor_dir = FLAVOR_DIR_MAP[args.flavor]
        output_dir = str(
            PROJECT_ROOT / "app/src" / flavor_dir
            / "play/listings" / locale_dir / "graphics/phone-screenshots"
        )

    compose_all(args.raw_dir, args.flavor, args.locale, output_dir)


if __name__ == "__main__":
    main()
