package com.wojdor.memolki.util.billing

interface BillingHandler {
    val consumableProductIds: Set<String>
    val nonConsumableProductIds: Set<String>

    fun startConnection(listener: BillingStatusListener)
    fun ensureConnected()
    fun launchBillingFlow(product: BillingProduct)
    suspend fun isPurchased(productId: String): Boolean

    companion object {
        const val IAP_COINS_SMALL = "coins_small"
        const val IAP_COINS_BIG = "coins_big"
        const val IAP_UNLOCK_ALL_CARDS = "unlock_all_cards"
    }
}
