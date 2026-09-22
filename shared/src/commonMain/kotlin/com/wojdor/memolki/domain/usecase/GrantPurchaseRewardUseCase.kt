package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.datastore.billing.BillingLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.billing.BillingHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GrantPurchaseRewardUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
    private val billingLocalDataSource: BillingLocalDataSource
) : BaseParameterUseCase<GrantPurchaseRewardUseCase.Params, Unit>(coroutineDispatcher) {

    data class Params(val productId: String, val purchaseId: String)

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(params: Params): Flow<Result<Unit>> = flow {
        if (billingLocalDataSource.isRewardGranted(params.purchaseId)) {
            emit(Result.success(Unit))
            return@flow
        }
        when (params.productId) {
            BillingHandler.IAP_COINS_SMALL ->
                userRepository.addCoins(BillingHandler.COINS_SMALL_REWARD)

            BillingHandler.IAP_COINS_BIG ->
                userRepository.addCoins(BillingHandler.COINS_BIG_REWARD)

            BillingHandler.IAP_UNLOCK_ALL_CARDS ->
                cardRepository.unlockAllCardPairs()

            else -> {
                emit(Result.failure(IllegalArgumentException("Unknown product id: ${params.productId}")))
                return@flow
            }
        }
        billingLocalDataSource.markRewardGranted(params.purchaseId)
        emit(Result.success(Unit))
    }
}
