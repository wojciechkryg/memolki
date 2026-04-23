package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine

class CalculateNextCardPairCostUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase,
    private val getBoardsUseCase: GetBoardsUseCase,
    private val cardRepository: CardRepository
) : BaseUseCase<Int>(coroutineDispatcher) {

    override fun execute() =
        combine(
            getBoardsUseCase(),
            getUnlockedCardPairsCountUseCase()
        ) { levelsResult, unlockedCardPairsCountResult ->
            val levels = levelsResult.getOrThrow()
            val unlockedCardPairsCount = unlockedCardPairsCountResult.getOrThrow()
            Result.success(calculateNextCardPairCost(levels, unlockedCardPairsCount))
        }

    private fun calculateNextCardPairCost(
        levels: List<BoardModel>,
        unlockedCardPairsCount: Int
    ): Int {
        val allPossibleCardPairsCount = cardRepository.getAllCardPairs().size
        if (unlockedCardPairsCount >= allPossibleCardPairsCount) return NO_MORE_CARDS

        if (unlockedCardPairsCount <= INITIAL_UNLOCKED_PAIRS) return MINIMUM_CARD_PAIR_COST
        val biggestUnlockedBoard =
            levels.filter { it.isUnlocked }.maxByOrNull { it.columns * it.rows }
                ?: return MINIMUM_CARD_PAIR_COST
        val levelPairsCount = (biggestUnlockedBoard.columns * biggestUnlockedBoard.rows) / 2
        return (BASE_COST + unlockedCardPairsCount * levelPairsCount / COST_DIVISOR)
            .coerceAtLeast(MINIMUM_CARD_PAIR_COST)
    }

    companion object {
        const val NO_MORE_CARDS = Int.MAX_VALUE
        const val MINIMUM_CARD_PAIR_COST = 1
        private const val BASE_COST = 0
        private const val COST_DIVISOR = 5
        private const val INITIAL_UNLOCKED_PAIRS = 5
    }
}
