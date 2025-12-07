package com.wojdor.memolki.ui.feature.moreapps

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.UiEffect

sealed class MoreAppsEffect : UiEffect {
    data class ShowAppInstall(val app: AppModel) : MoreAppsEffect()
    data class OpenApp(val app: AppModel) : MoreAppsEffect()
}
