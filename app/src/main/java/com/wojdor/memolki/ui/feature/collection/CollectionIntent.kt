package com.wojdor.memolki.ui.feature.collection

import com.wojdor.memolki.ui.base.UiIntent

sealed class CollectionIntent : UiIntent {
    object OnShopClick : CollectionIntent()
}
