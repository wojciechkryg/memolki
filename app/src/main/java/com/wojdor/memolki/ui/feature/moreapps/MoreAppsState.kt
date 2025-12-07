package com.wojdor.memolki.ui.feature.moreapps

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoreAppsState(
    val apps: List<AppModel> = emptyList(),
) : UiState
