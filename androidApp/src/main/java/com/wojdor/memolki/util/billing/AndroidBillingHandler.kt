package com.wojdor.memolki.util.billing

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
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
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_BIG
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_COINS_SMALL
import com.wojdor.memolki.util.billing.BillingHandler.Companion.IAP_UNLOCK_ALL_CARDS
import com.wojdor.memolki.util.extension.logE
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.ref.WeakReference
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import kotlin.coroutines.resume

open class AndroidBillingHandler(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : BillingHandler, PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private val scope = CoroutineScope(dispatcher)
    private val connectionLock = Any()
    private var listener: BillingStatusListener? = null
    private var cachedProducts: List<ProductDetails> = emptyList()
    private var currentActivityRef: WeakReference<Activity>? = null
    @Volatile
    private var connectionReady = CompletableDeferred<Boolean>()

    override val consumableProductIds = setOf(IAP_COINS_SMALL, IAP_COINS_BIG)
    override val nonConsumableProductIds = setOf(IAP_UNLOCK_ALL_CARDS)

    init {
        (context.applicationContext as? Application)?.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityResumed(activity: Activity) {
                    currentActivityRef = WeakReference(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    if (currentActivityRef?.get() === activity) currentActivityRef = null
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
                override fun onActivityDestroyed(activity: Activity) = Unit
            }
        )
    }

    override fun startConnection(listener: BillingStatusListener) {
        this.listener = listener
        if (::billingClient.isInitialized && billingClient.isReady) {
            listener.onConnectionStatusChanged(true)
            if (cachedProducts.isNotEmpty()) {
                listener.onProductsFetched(cachedProducts.map(ProductDetails::toBillingProduct))
            } else {
                scope.launch { queryProductDetails() }
            }
            return
        }
        connectInternal()
    }

    override fun ensureConnected() {
        if (::billingClient.isInitialized && billingClient.isReady) return
        connectInternal()
    }

    private fun connectInternal() {
        synchronized(connectionLock) {
            if (::billingClient.isInitialized && billingClient.isReady) return
            if (::billingClient.isInitialized && !connectionReady.isCompleted) return
            connectionReady = CompletableDeferred()
            if (!::billingClient.isInitialized) {
                billingClient = BillingClient.newBuilder(context)
                    .setListener(this)
                    .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
                    )
                    .build()
            }
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        if (!connectionReady.isCompleted) connectionReady.complete(true)
                        listener?.onConnectionStatusChanged(true)
                        scope.launch {
                            queryProductDetails()
                            queryExistingPurchases()
                        }
                    } else {
                        if (!connectionReady.isCompleted) connectionReady.complete(false)
                        listener?.onConnectionStatusChanged(false)
                        logE("Billing connection error.", Exception(billingResult.debugMessage))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    listener?.onConnectionStatusChanged(false)
                    scope.launch {
                        delay(RETRY_CONNECTION_DELAY)
                        connectInternal()
                    }
                }
            })
        }
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
            val products = result.productDetailsList.orEmpty()
            cachedProducts = products
            listener?.onProductsFetched(products.map(ProductDetails::toBillingProduct))
        } else {
            logE("Error fetching products.", Exception(result.billingResult.debugMessage))
        }
    }

    override fun launchBillingFlow(product: BillingProduct) {
        val activity = currentActivityRef?.get() ?: run {
            listener?.onPurchaseFailed()
            return
        }
        if (!::billingClient.isInitialized || !billingClient.isReady) {
            listener?.onPurchaseFailed()
            return
        }
        val productDetails = cachedProducts.firstOrNull { it.productId == product.id } ?: run {
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

    private fun queryExistingPurchases() {
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
        val result = billingClient.consumePurchase(consumeParams)
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

    override suspend fun isPurchased(productId: String): Boolean {
        if (!::billingClient.isInitialized) return false
        val ready = connectionReady
        withTimeoutOrNull(READY_TIMEOUT_MS) { ready.await() }
        if (!billingClient.isReady) return false
        return suspendCancellableCoroutine { continuation ->
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val isPurchased =
                        purchases.any { it.products.contains(productId) && it.isAcknowledged }
                    continuation.resume(isPurchased)
                } else {
                    continuation.resume(false)
                }
            }
        }
    }

    private companion object {
        private const val FAKE_DATA = "intentionally_wrong_transaction_token"
        private const val FAKE_SIGNATURE = "YmFkX3NpZ25hdHVyZ_ZmFrZQ=="
        private const val FAKE_PUBLIC_KEY = "FAKE_PUBLIC_KEY"
        private const val RETRY_CONNECTION_DELAY = 5000L
        private const val READY_TIMEOUT_MS = 5000L
    }
}

private fun ProductDetails.toBillingProduct(): BillingProduct {
    val offer = oneTimePurchaseOfferDetails
    return BillingProduct(
        id = productId,
        formattedPrice = offer?.formattedPrice.orEmpty(),
        priceMicros = offer?.priceAmountMicros ?: 0L,
        currencyCode = offer?.priceCurrencyCode.orEmpty()
    )
}
