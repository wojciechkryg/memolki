package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnlockRandomCardIfEnoughCoinsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase,
    private val cardRepository: CardRepository,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        val nextCardPairCost = calculateNextCardPairCostUseCase().first().getOrThrow()
        val userCoins = userRepository.getCoins().first()
        if (userCoins < nextCardPairCost) {
            emit(Result.failure(IllegalStateException("Not enough coins to unlock a new card pair. User coins: $userCoins, next card pair cost: $nextCardPairCost")))
            return@flow
        }
        userRepository.removeCoins(nextCardPairCost.toLong())
        val notUnlockedCardPairs = cardRepository.getAllCardPairs()
            .filter { cardPair ->
                cardPair.first.pairId !in cardRepository.getUnlockedCardPairs()
                    .map { it.first.pairId }
            }
        val randomCardPairToUnlock = notUnlockedCardPairs.random()
        cardRepository.addUnlockedCardPairId(randomCardPairToUnlock.first.pairId)
        emit(Result.success(Unit))
    }
}
