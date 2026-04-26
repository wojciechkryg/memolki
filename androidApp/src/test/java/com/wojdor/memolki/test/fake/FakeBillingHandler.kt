package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS
import com.wojdor.memolki.util.billing.BillingProduct
import com.wojdor.memolki.util.billing.BillingStatusListener

class FakeBillingHandler : BillingHandler {
    override val consumableProductIds: Set<String> = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override val nonConsumableProductIds: Set<String> = setOf(IAP_UNLOCK_ALL_CARDS)

    var purchasedProducts: MutableSet<String> = mutableSetOf()

    override fun startConnection(listener: BillingStatusListener) {
        listener.onConnectionStatusChanged(true)
    }

    override fun ensureConnected() = Unit

    override fun launchBillingFlow(product: BillingProduct) = Unit

    override suspend fun isPurchased(productId: String): Boolean =
        productId in purchasedProducts
}
