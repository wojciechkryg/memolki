package com.wojdor.memolki.ui.feature.moreapps

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class MoreAppsIntent : UiIntent {
    data class OnAppClick(val app: AppModel) : MoreAppsIntent()
}
