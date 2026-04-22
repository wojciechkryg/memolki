package com.wojdor.memolki.util.billing

interface BillingStatusListener {
    fun onProductsFetched(products: List<BillingProduct>)
    fun onPurchaseSuccessful(productId: String)
    fun onPurchaseFailed()
    fun onConnectionStatusChanged(isConnected: Boolean)
}
