package com.wojdor.memolki.util.billing

import android.app.Activity
import android.content.Context
import android.util.Base64
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.wojdor.memolki.BuildConfig
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.util.extension.logD
import com.wojdor.memolki.util.extension.logE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import javax.inject.Inject

class BillingHandler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private val scope = CoroutineScope(dispatcher)
    private var listener: BillingStatusListener? = null

    val consumableProductIds = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    val nonConsumableProductIds = setOf(IAP_UNLOCK_ALL_CARDS)

    init {
        startConnection()
    }

    fun setListener(listener: BillingStatusListener) {
        this.listener = listener
    }

    private fun startConnection() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    listener?.onConnectionStatusChanged(true)
                    scope.launch {
                        queryProductDetails()
                        queryExistingPurchases()
                    }
                } else {
                    listener?.onConnectionStatusChanged(false)
                    logE("Billing connection error.", Exception(billingResult.debugMessage))
                }
            }

            override fun onBillingServiceDisconnected() {
                listener?.onConnectionStatusChanged(false)
                logD("Billing disconnection. Retrying to connect.")
                scope.launch {
                    delay(RETRY_CONNECTION_DELAY)
                    startConnection()
                }
            }
        })
    }

    private suspend fun queryProductDetails() {
        if (!billingClient.isReady) return
        val allProductIds = consumableProductIds + nonConsumableProductIds
        val productList = allProductIds.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        val result = billingClient.queryProductDetails(params)
        if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            result.productDetailsList?.let { listener?.onProductsFetched(it) }
        } else {
            logE("Error fetching products.", Exception(result.billingResult.debugMessage))
        }
    }

    fun launchBillingFlow(
        activity: Activity,
        productDetails: ProductDetails
    ) {
        if (!billingClient.isReady) {
            listener?.onPurchaseFailed()
            return
        }
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            scope.launch {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        } else {
            listener?.onPurchaseFailed()
        }
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            listener?.onPurchaseFailed()
            return
        }
        val productId = purchase.products.firstOrNull() ?: return
        if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
            listener?.onPurchaseFailed()
            return
        }
        if (verifyHackedSignature()) {
            listener?.onPurchaseFailed()
            return
        }

        if (consumableProductIds.contains(productId)) {
            consumePurchase(purchase)
        } else if (nonConsumableProductIds.contains(productId)) {
            if (!purchase.isAcknowledged) {
                acknowledgePurchase(purchase)
            } else {
                listener?.onPurchaseSuccessful(productId)
            }
        }
    }

    fun queryExistingPurchases() {
        if (!billingClient.isReady) return

        billingClient.queryPurchasesAsync(
            QueryPurchasesParams
                .newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                scope.launch {
                    for (purchase in purchasesList) {
                        handlePurchase(purchase)
                    }
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { result ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                listener?.onPurchaseSuccessful(purchase.products.first())
            } else {
                listener?.onPurchaseFailed()
            }
        }
    }

    private suspend fun consumePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val result = withContext(dispatcher) {
            billingClient.consumePurchase(consumeParams)
        }
        if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            listener?.onPurchaseSuccessful(purchase.products.first())
        } else {
            listener?.onPurchaseFailed()
        }
    }

    private fun verifyValidSignature(
        signedData: String,
        signature: String
    ) = verifySignature(
        signedData,
        signature,
        BuildConfig.BILLING_KEY
    )

    private fun verifyHackedSignature() =
        verifySignature(
            FAKE_DATA,
            FAKE_SIGNATURE,
            FAKE_PUBLIC_KEY
        )

    private fun verifySignature(
        signedData: String,
        signature: String,
        base64PublicKey: String
    ): Boolean = try {
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(Base64.decode(base64PublicKey, Base64.DEFAULT))
        val publicKey: PublicKey = keyFactory.generatePublic(keySpec)
        val signatureObj = Signature.getInstance("SHA1withRSA")
        signatureObj.initVerify(publicKey)
        signatureObj.update(signedData.toByteArray(StandardCharsets.UTF_8))
        signatureObj.verify(Base64.decode(signature, Base64.DEFAULT))
    } catch (error: Exception) {
        logE("Error verifying signature.", error)
        false
    }

    companion object {
        const val IAP_COINS_SMALL = "coins_small"
        const val IAP_COINS_BIG = "coins_big"
        const val IAP_UNLOCK_ALL_CARDS = "unlock_all_cards"

        private const val FAKE_DATA = "intentionally_wrong_transaction_token"
        private const val FAKE_SIGNATURE = "YmFkX3NpZ25hdHVyZ_ZmFrZQ=="
        private const val FAKE_PUBLIC_KEY = "FAKE_PUBLIC_KEY"
        private const val RETRY_CONNECTION_DELAY = 5000L
    }
}
