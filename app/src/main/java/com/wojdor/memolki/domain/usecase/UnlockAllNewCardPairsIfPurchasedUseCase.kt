package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnlockAllNewCardPairsIfPurchasedUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase,
    private val cardRepository: CardRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        if (cardRepository.getLockedCardPairs().isNotEmpty()
            && cardRepository.areAllCardPairsUnlocked()
        ) {
            unlockAllCardPairsUseCase().collect { result ->
                result.onSuccess {
                    emit(Result.success(Unit))
                }.onFailure { error ->
                    emit(Result.failure(error))
                }
            }
        } else {
            emit(Result.success(Unit))
        }
    }
}
