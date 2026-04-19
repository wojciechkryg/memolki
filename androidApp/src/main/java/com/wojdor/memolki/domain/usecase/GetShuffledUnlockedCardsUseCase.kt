package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class GetShuffledUnlockedCardsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository,
    private val random: Random
) : BaseParameterUseCase<BoardModel, List<CardModel>>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(board: BoardModel) = flow {
        val cardPairIdsCount = (board.rows * board.columns) / 2
        val randomUnlockedCardPairIds =
            cardRepository.getRandomUnlockedCardPairIds(cardPairIdsCount)
        val shuffledCards = randomUnlockedCardPairIds.mapNotNull {
            cardRepository.getCardPairById(it)
        }
            .flatMap { listOf(it.first, it.second) }
            .shuffled(random)
        emit(Result.success(shuffledCards))
    }
}
