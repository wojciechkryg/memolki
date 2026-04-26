package com.wojdor.memolki.util.extension

// TODO(kmp-ios): iOS has no system toast; consider a UIView-based banner overlay when iOS UI ships.
class IosToaster : Toaster {
    override fun show(text: String) = Unit
}
