package com.wojdor.memolki.util.billing

interface IosStoreKitBridge {
    fun products(productIds: List<String>, completion: (List<BillingProduct>) -> Unit)
    fun purchase(productId: String, completion: (Boolean) -> Unit)
    fun isPurchased(productId: String, completion: (Boolean) -> Unit)

    // The bridge invokes the handler for every verified transaction (fresh purchases and
    // redelivered unfinished ones). Call finishTransaction only after the reward is safely
    // granted, so an interrupted grant is redelivered on the next launch.
    fun setTransactionHandler(handler: (productId: String, transactionId: String) -> Unit)
    fun finishTransaction(transactionId: String)
}
