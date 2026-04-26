package com.wojdor.memolki.util.extension

// TODO(kmp-ios): replace with UIActivityViewController-backed share sheet when iOS share ships.
class IosTextSharer : TextSharer {
    override fun share(text: String) = Unit
}
