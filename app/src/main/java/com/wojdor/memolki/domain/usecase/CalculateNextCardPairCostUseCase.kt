package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

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
        val allPossibleCardPairsCount = cardRepository.getAllCardPairs().size
        if (unlockedCardPairsCount >= allPossibleCardPairsCount) return NO_MORE_CARDS

        val biggestUnlockedLevel = levels.filter { it.isUnlocked }.maxByOrNull { it.columns * it.rows }
            ?: return MINIMUM_CARD_PAIR_COST
        val levelPairsCount = (biggestUnlockedLevel.columns * biggestUnlockedLevel.rows) / 2
        return (BASE_COST + unlockedCardPairsCount * levelPairsCount / COST_DIVISOR)
            .coerceAtLeast(MINIMUM_CARD_PAIR_COST)
    }

    companion object {
        const val NO_MORE_CARDS = Int.MAX_VALUE
        const val MINIMUM_CARD_PAIR_COST = 1
        private const val BASE_COST = 0
        private const val COST_DIVISOR = 5
    }
}
