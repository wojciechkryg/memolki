package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UnlockRandomCardIfEnoughCoinsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase,
    private val unlockRandomCardUseCase: UnlockRandomCardUseCase,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        val nextCardPairCost = calculateNextCardPairCostUseCase().first().getOrThrow()
        val userCoins = userRepository.getCoins().first()
        if (userCoins < nextCardPairCost) {
            emit(Result.failure(IllegalStateException("Not enough coins to unlock a new card pair. User coins: $userCoins, next card pair cost: $nextCardPairCost")))
        } else {
            unlockRandomCardUseCase().first()
                .onSuccess {
                    userRepository.removeCoins(nextCardPairCost.toLong())
                    emit(Result.success(Unit))
                }.onFailure {
                    emit(Result.failure(it))
                }
        }
    }
}
