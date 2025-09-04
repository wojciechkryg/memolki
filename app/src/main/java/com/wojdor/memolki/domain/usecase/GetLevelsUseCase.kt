package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLevelsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase
) : BaseUseCase<List<LevelModel>>(coroutineDispatcher) {

    override fun execute() =
        getUnlockedCardPairsCountUseCase().map { result ->
            result.map { unlockedCardPairsCount ->
                prepareLevels(unlockedCardPairsCount)
            }
        }

    private fun prepareLevels(unlockedCardPairsCount: Int): List<LevelModel> {
        return listOf(
            unlockLevelIfNeeded(LevelModel.Grid2x3(), unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid3x4(), unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid4x4(), unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid4x5(), unlockedCardPairsCount),
            unlockLevelIfNeeded(LevelModel.Grid5x6(), unlockedCardPairsCount)
        )
    }

    private fun unlockLevelIfNeeded(level: LevelModel, unlockedCardPairsCount: Int): LevelModel {
        val requiredCardPairsCount = (level.columns * level.rows) / 2
        return if (unlockedCardPairsCount >= requiredCardPairsCount) {
            when (level) {
                LevelModel.Empty -> level
                is LevelModel.Grid2x3 -> level.copy(isUnlocked = true)
                is LevelModel.Grid3x4 -> level.copy(isUnlocked = true)
                is LevelModel.Grid4x4 -> level.copy(isUnlocked = true)
                is LevelModel.Grid4x5 -> level.copy(isUnlocked = true)
                is LevelModel.Grid5x6 -> level.copy(isUnlocked = true)
            }
        } else {
            level
        }
    }
}
