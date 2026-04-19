package com.wojdor.memolki.ui.feature.moreapps

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.serialization.Serializable

@Serializable
data class MoreAppsState(
    val apps: List<AppModel> = emptyList(),
) : UiState
