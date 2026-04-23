package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetMenuUseCase(
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<List<MenuModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val menuItems = listOf<MenuModel>(
            MenuModel.Play,
            MenuModel.Collection,
            MenuModel.Leaderboard,
            MenuModel.Settings
        )
        emit(Result.success(menuItems))
    }
}
