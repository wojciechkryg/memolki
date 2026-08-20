package com.wojdor.memolki.util.billing

import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosBillingHandler(
    private val bridge: IosStoreKitBridge
) : BillingHandler {

    override val consumableProductIds: Set<String> = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override val nonConsumableProductIds: Set<String> = setOf(IAP_UNLOCK_ALL_CARDS)

    private var listener: BillingStatusListener? = null

    override fun startConnection(listener: BillingStatusListener) {
        this.listener = listener
        listener.onConnectionStatusChanged(true)
        val allProductIds = (consumableProductIds + nonConsumableProductIds).toList()
        bridge.products(allProductIds) { products ->
            this.listener?.onProductsFetched(products)
        }
    }

    override fun ensureConnected() = Unit

    override fun launchBillingFlow(product: BillingProduct) {
        bridge.purchase(product.id) { success ->
            if (success) listener?.onPurchaseSuccessful(product.id) else listener?.onPurchaseFailed()
        }
    }

    override suspend fun isPurchased(productId: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            bridge.isPurchased(productId) { purchased ->
                if (continuation.isActive) continuation.resume(purchased)
            }
        }
}
