package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.billing.BillingHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UnlockAllNewCardPairsIfPurchasedUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val billingHandler: BillingHandler,
    private val cardRepository: CardRepository,
    private val unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        if (cardRepository.getLockedCardPairs().isNotEmpty()
            && billingHandler.isPurchased(BillingHandler.IAP_UNLOCK_ALL_CARDS)
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
