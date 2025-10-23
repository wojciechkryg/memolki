package com.wojdor.memolki.util.billing

import com.android.billingclient.api.ProductDetails

interface BillingStatusListener {
    fun onProductsFetched(products: List<ProductDetails>)
    fun onPurchaseSuccessful(productId: String)
    fun onPurchaseFailed()
    fun onConnectionStatusChanged(isConnected: Boolean)
}
