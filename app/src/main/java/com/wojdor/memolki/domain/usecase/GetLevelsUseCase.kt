package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLevelsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<List<LevelModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val menuItems = listOf(
            LevelModel.Level2x3,
            LevelModel.Level3x4,
            LevelModel.Level4x4,
            LevelModel.Level4x5,
            LevelModel.Level5x6,
        )
        emit(Result.success(menuItems))
    }
}
