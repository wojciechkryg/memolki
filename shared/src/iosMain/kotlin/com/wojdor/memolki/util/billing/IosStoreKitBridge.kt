package com.wojdor.memolki.util.billing

interface IosStoreKitBridge {
    fun products(productIds: List<String>, completion: (List<BillingProduct>) -> Unit)
    fun purchase(productId: String, completion: (Boolean) -> Unit)
    fun isPurchased(productId: String, completion: (Boolean) -> Unit)
}
