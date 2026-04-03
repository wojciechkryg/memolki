package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class GetDailyChallengeCardsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository,
    private val timeProvider: TimeProvider
) : BaseParameterUseCase<LevelModel, List<CardModel>>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(level: LevelModel) = flow {
        val pairCount = (level.columns * level.rows) / 2
        val seed = timeProvider.currentLocalDate().toEpochDay()
        val random = Random(seed)
        val allCardPairIds = cardRepository.getAllCardPairIds()
        require(allCardPairIds.size >= pairCount) {
            "Not enough card pairs for $level. Required=$pairCount, available=${allCardPairIds.size}"
        }
        val selectedPairIds = allCardPairIds.shuffled(random).take(pairCount)
        val shuffledCards = selectedPairIds.map { id ->
            requireNotNull(cardRepository.getCardPairById(id)) { "Missing card pair for id=$id" }
        }
            .flatMap { listOf(it.first, it.second) }
            .shuffled(random)
        emit(Result.success(shuffledCards))
    }
}
