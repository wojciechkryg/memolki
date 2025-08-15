package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMenuUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<List<MenuModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val menuItems = listOf(
            MenuModel.NewGame,
            MenuModel.Collection,
            MenuModel.Settings
        )
        emit(Result.success(menuItems))
    }
}
