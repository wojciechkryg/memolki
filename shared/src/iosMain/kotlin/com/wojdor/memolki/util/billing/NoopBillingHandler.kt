package com.wojdor.memolki.util.billing

import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS

// TODO(kmp-ios): replace with StoreKit-backed impl when iOS billing ships.
class NoopBillingHandler : BillingHandler {
    override val consumableProductIds: Set<String> = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override val nonConsumableProductIds: Set<String> = setOf(IAP_UNLOCK_ALL_CARDS)

    override fun startConnection(listener: BillingStatusListener) {
        listener.onConnectionStatusChanged(false)
    }

    override fun ensureConnected() = Unit
    override fun launchBillingFlow(product: BillingProduct) = Unit
    override suspend fun isPurchased(productId: String) = false
}
