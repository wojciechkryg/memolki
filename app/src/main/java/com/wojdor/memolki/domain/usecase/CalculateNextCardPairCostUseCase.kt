package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.math.log2

class CalculateNextCardPairCostUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase,
    private val getLevelsUseCase: GetLevelsUseCase,
    private val cardRepository: CardRepository
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
        val biggestUnlockedLevel = unlockedLevels.maxBy { it.columns * it.rows }
        val allPossibleCardPairsCount = cardRepository.getAllCardPairs().size
        val missingCardPairsCountToCalculate = allPossibleCardPairsCount - unlockedCardPairsCount

        return if (missingCardPairsCountToCalculate > 0) {
            val baseCardPairCost = getBaseCardPairCost(biggestUnlockedLevel)
            val biggestUnlockedLevelCardPairsCount = getCardPairsCountForLevel(biggestUnlockedLevel)
            val unlockedLevelAdditionalCost =
                (biggestUnlockedLevelCardPairsCount.toDouble() * CARD_PAIR_COST_FACTOR).toInt()
            val unlockedCardPairsAdditionalCount = missingCardPairsCountToCalculate
            val baseLevelCalculation = baseCardPairCost * log2(
                biggestUnlockedLevelCardPairsCount.toDouble()
            )
            (((baseLevelCalculation / BASE_LEVEL_FACTOR) + unlockedLevelAdditionalCost) * (biggestUnlockedLevelCardPairsCount / (unlockedCardPairsAdditionalCount + UNLOCKED_CARD_FACTOR).toDouble())).toInt()
        } else {
            NO_MORE_CARDS
        }
    }

    private fun getCardPairsCountForLevel(level: LevelModel): Int {
        return (level.columns * level.rows) / 2
    }

    private fun getBaseCardPairCost(level: LevelModel): Int {
        return (level.columns * level.rows * CARD_PAIR_COST_FACTOR).toInt()
    }

    companion object {
        const val NO_MORE_CARDS = Int.MAX_VALUE
        const val CARD_PAIR_COST_FACTOR = 3.5
        const val UNLOCKED_CARD_FACTOR = 4
        const val BASE_LEVEL_FACTOR = 1.7
    }
}
