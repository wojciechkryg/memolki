package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class GetShuffledUnlockedCardsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository,
    private val random: Random
) : BaseParameterUseCase<LevelModel, List<CardModel>>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(level: LevelModel) = flow {
        val cardPairIdsCount = (level.rows * level.columns) / 2
        val randomUnlockedCardPairIds =
            cardRepository.getRandomUnlockedCardPairIds(cardPairIdsCount)
        val shuffledCards = randomUnlockedCardPairIds.mapNotNull {
            cardRepository.getCardPairById(it)?.pair
        }
            .flatMap { it.toList() }
            .shuffled(random)
        emit(Result.success(shuffledCards))
    }
}
