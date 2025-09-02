package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLevelsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase
) : BaseUseCase<List<LevelModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        getUnlockedCardPairsCountUseCase().collect { result ->
            result.onSuccess { unlockedCardPairsCount ->
                emit(Result.success(prepareLevels(unlockedCardPairsCount)))
            }
        }
    }

    private fun prepareLevels(unlockedCardPairsCount: Int): List<LevelModel> {
        return listOf(
            unlockLevelIfNeeded(LevelModel.Grid2x3, unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid3x4, unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid4x4, unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid4x5, unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid5x6, unlockedCardPairsCount)
        )
    }

    private fun unlockLevelIfNeeded(level: LevelModel, unlockedCardPairsCount: Int): LevelModel {
        val requiredCardPairsCount = (level.columns * level.rows) / 2
        if (unlockedCardPairsCount >= requiredCardPairsCount) {
            level.unlock()
        }
        return level
    }
}
