package com.wojdor.memolki.ui.feature.menu

import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.ui.base.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuState(
    val menu: List<MenuModel> = emptyList()
) : UiState
