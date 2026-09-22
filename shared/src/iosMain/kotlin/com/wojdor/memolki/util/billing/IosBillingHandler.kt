package com.wojdor.memolki.util.billing

import com.wojdor.memolki.domain.usecase.GrantPurchaseRewardUseCase
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosBillingHandler(
    private val bridge: IosStoreKitBridge,
    private val grantPurchaseRewardUseCase: GrantPurchaseRewardUseCase,
    dispatcher: CoroutineDispatcher
) : BillingHandler {

    override val consumableProductIds: Set<String> = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override val nonConsumableProductIds: Set<String> = setOf(IAP_UNLOCK_ALL_CARDS)

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private var listener: BillingStatusListener? = null

    init {
        bridge.setTransactionHandler { productId, transactionId ->
            scope.launch {
                grantPurchaseRewardUseCase(
                    GrantPurchaseRewardUseCase.Params(productId, transactionId)
                ).first()
                    .onSuccess {
                        bridge.finishTransaction(transactionId)
                        listener?.onPurchaseSuccessful(productId)
                    }
                    .onFailure {
                        // Transaction stays unfinished — StoreKit redelivers it on next launch.
                        listener?.onPurchaseFailed()
                    }
            }
        }
    }

    override fun startConnection(listener: BillingStatusListener) {
        this.listener = listener
        listener.onConnectionStatusChanged(true)
        val allProductIds = (consumableProductIds + nonConsumableProductIds).toList()
        bridge.products(allProductIds) { products ->
            this.listener?.onProductsFetched(products)
        }
    }

    override fun clearListener() {
        listener = null
    }

    override fun ensureConnected() = Unit

    override fun launchBillingFlow(product: BillingProduct) {
        bridge.purchase(product.id) { initiated ->
            // The reward and success callback flow through the transaction handler.
            if (!initiated) listener?.onPurchaseFailed()
        }
    }

    override suspend fun isPurchased(productId: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            bridge.isPurchased(productId) { purchased ->
                if (continuation.isActive) continuation.resume(purchased)
            }
        }
}
