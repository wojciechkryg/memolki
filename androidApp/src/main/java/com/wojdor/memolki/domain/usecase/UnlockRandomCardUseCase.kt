package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class UnlockRandomCardUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository,
    private val random: Random
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        val lockedCardPairs = cardRepository.getLockedCardPairs()
        if (lockedCardPairs.isEmpty()) {
            emit(Result.failure(IllegalStateException("All card pairs are already unlocked")))
        } else {
            val randomCardPairToUnlock = lockedCardPairs.random(random)
            cardRepository.addUnlockedCardPairId(randomCardPairToUnlock.first.pairId)
            emit(Result.success(Unit))
        }
    }
}
