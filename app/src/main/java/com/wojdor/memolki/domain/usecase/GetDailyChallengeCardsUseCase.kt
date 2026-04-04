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
        val allCardPairs = cardRepository.getAllCardPairsWithAddedEpochDay()
        require(allCardPairs.size >= pairCount) {
            "Not enough card pairs for $level. Required=$pairCount, available=${allCardPairs.size}"
        }
        val selectedPairIds = allCardPairs
            .map { (id, addedEpochDay) ->
                val daysSinceAdded = seed - addedEpochDay
                val score = if (daysSinceAdded < NEW_CARD_GRACE_PERIOD_DAYS) {
                    0.0
                } else {
                    Random(seed * 31 + id.hashCode().toLong()).nextDouble()
                }
                id to score
            }
            .sortedByDescending { it.second }
            .take(pairCount)
            .map { it.first }
        val boardRandom = Random(seed)
        val shuffledCards = selectedPairIds.map { id ->
            requireNotNull(cardRepository.getCardPairById(id)) { "Missing card pair for id=$id" }
        }
            .flatMap { listOf(it.first, it.second) }
            .shuffled(boardRandom)
        emit(Result.success(shuffledCards))
    }

    companion object {
        private const val NEW_CARD_GRACE_PERIOD_DAYS = 21
    }
}
