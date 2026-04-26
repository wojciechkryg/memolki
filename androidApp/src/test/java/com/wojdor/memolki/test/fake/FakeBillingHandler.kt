package com.wojdor.memolki.test.fake

import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS
import com.wojdor.memolki.util.billing.BillingProduct
import com.wojdor.memolki.util.billing.BillingStatusListener

class FakeBillingHandler : BillingHandler {
    override var consumableProductIds: Set<String> = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override var nonConsumableProductIds: Set<String> = setOf(IAP_UNLOCK_ALL_CARDS)

    var purchasedProducts: MutableSet<String> = mutableSetOf()
    var capturedListener: BillingStatusListener? = null
        private set
    var ensureConnectedCount: Int = 0
        private set

    override fun startConnection(listener: BillingStatusListener) {
        capturedListener = listener
    }

    override fun ensureConnected() {
        ensureConnectedCount++
    }

    override fun launchBillingFlow(product: BillingProduct) = Unit

    override suspend fun isPurchased(productId: String): Boolean =
        productId in purchasedProducts
}
