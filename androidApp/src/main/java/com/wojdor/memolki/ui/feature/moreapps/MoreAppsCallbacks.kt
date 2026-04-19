package com.wojdor.memolki.ui.feature.moreapps

import com.wojdor.memolki.domain.model.AppModel

data class MoreAppsCallbacks(
    val onAppClick: (AppModel) -> Unit = {}
)
