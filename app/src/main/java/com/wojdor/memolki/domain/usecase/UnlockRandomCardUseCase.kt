package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnlockRandomCardUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        val unlockedIds = cardRepository.getUnlockedCardPairs()
            .map { it.first.pairId }
            .toSet()
        val notUnlockedCardPairs = cardRepository.getAllCardPairs()
            .filter { it.first.pairId !in unlockedIds }
        if (notUnlockedCardPairs.isEmpty()) {
            emit(Result.failure(IllegalStateException("All card pairs are already unlocked")))
        } else {
            val randomCardPairToUnlock = notUnlockedCardPairs.random()
            cardRepository.addUnlockedCardPairId(randomCardPairToUnlock.first.pairId)
            emit(Result.success(Unit))
        }
    }
}
