package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.math.ceil

class CalculateNextCardPairCostUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase,
    private val getLevelsUseCase: GetLevelsUseCase
) : BaseUseCase<Int>(coroutineDispatcher) {

    override fun execute() =
        combine(
            getLevelsUseCase(),
            getUnlockedCardPairsCountUseCase()
        ) { levelsResult, unlockedCardPairsCountResult ->
            runCatching {
                val levels = levelsResult.getOrThrow()
                val unlockedCardPairsCount = unlockedCardPairsCountResult.getOrThrow()
                calculateNextCardPairCost(levels, unlockedCardPairsCount)
            }
        }

    private fun calculateNextCardPairCost(
        levels: List<LevelModel>,
        unlockedCardPairsCount: Int
    ): Int {
        val unlockedLevels = levels.filter { it.isUnlocked }
        val lockedLevels = levels.filter { !it.isUnlocked }
        val nextLockedLevel = lockedLevels.minBy { it.columns * it.rows }
        val biggestUnlockedLevel = unlockedLevels.maxBy { it.columns * it.rows }
        val biggestUnlockedLevelCardPairsCount = getCardPairsCount(biggestUnlockedLevel)
        val nextLockedLevelCardPairsCount = getCardPairsCount(nextLockedLevel)
        val missingCardPairsCountToNextLockedLevel = nextLockedLevelCardPairsCount - unlockedCardPairsCount
        return if (missingCardPairsCountToNextLockedLevel > 0) {
            val additionalCost =
                (biggestUnlockedLevelCardPairsCount.toDouble() * CARD_PAIR_COST_FACTOR) / missingCardPairsCountToNextLockedLevel
            getBaseCardPairCost(biggestUnlockedLevel) + ceil(additionalCost).toInt()
        } else {
            NO_MORE_CARDS
        }
    }

    private fun getCardPairsCount(level: LevelModel): Int {
        return (level.columns * level.rows) / 2
    }

    private fun getBaseCardPairCost(level: LevelModel): Int {
        return level.columns * level.rows
    }

    companion object {
        const val NO_MORE_CARDS = -1
        const val CARD_PAIR_COST_FACTOR = 3
    }
}
