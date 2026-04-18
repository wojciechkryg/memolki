package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.domain.usecase.CalculateCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.CheckDailyLoginStreakUseCase
import com.wojdor.memolki.domain.usecase.CollectDailyStreakRewardUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetTotalCoinsUseCase
import com.wojdor.memolki.domain.usecase.IsShopAdCooldownOverUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopPurchaseUseCase
import com.wojdor.memolki.domain.usecase.ScheduleAdRewardNotificationUseCase
import com.wojdor.memolki.domain.usecase.SetLastShopAdShownTimestampUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllCardPairsUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.ui.feature.shop.ShopEffect.SendTotalCoinsScore
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingStatusListener
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.GooglePlayGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analytics: Analytics,
    private val hapticFeedback: HapticFeedback,
    private val coinsPlayer: CoinsPlayer,
    private val allRewardedAds: AllRewardedAds,
    private val billingHandler: BillingHandler,
    private val googlePlayGames: GooglePlayGames,
    private val isShopAdCooldownOverUseCase: IsShopAdCooldownOverUseCase,
    private val setLastShopAdShownTimestampUseCase: SetLastShopAdShownTimestampUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val calculateCoinsForShopAdUseCase: CalculateCoinsForShopAdUseCase,
    private val rewardCoinsForShopAdUseCase: RewardCoinsForShopAdUseCase,
    private val rewardCoinsForShopPurchaseUseCase: RewardCoinsForShopPurchaseUseCase,
    private val unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase,
    private val getTotalCoinsUseCase: GetTotalCoinsUseCase,
    private val scheduleAdRewardNotificationUseCase: ScheduleAdRewardNotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
    private val checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase,
    private val collectDailyStreakRewardUseCase: CollectDailyStreakRewardUseCase
) : MviViewModel<ShopIntent, ShopState>(
    savedStateHandle,
    ShopState()
) {

    private var loadCoinsJob: Job? = null
    private var checkStreakJob: Job? = null
    private var productDetails: List<ProductDetails> = emptyList()
    private var dailyStreakResult: CheckDailyLoginStreakUseCase.DailyStreakResult? = null
    private val priceByProductId: Map<String, String>
        get() = productDetails.associateBy({ it.productId }) { product ->
            product.oneTimePurchaseOfferDetails?.formattedPrice.orEmpty()
        }

    init {
        checkDailyStreak()
        loadAdCoins()
        loadData()
        billingHandler.startConnection(object : BillingStatusListener {
            override fun onProductsFetched(products: List<ProductDetails>) {
                this@ShopViewModel.onProductsFetched(products)
            }

            override fun onPurchaseSuccessful(productId: String) {
                this@ShopViewModel.onPurchaseSuccessful(productId)
            }

            override fun onPurchaseFailed() {
                this@ShopViewModel.onPurchaseFailed()
            }

            override fun onConnectionStatusChanged(isConnected: Boolean) {
                this@ShopViewModel.onConnectionStatusChanged(isConnected)
            }
        })
    }

    override fun onIntent(intent: ShopIntent) {
        when (intent) {
            ShopIntent.OnWatchAdClick -> onWatchAdClick()
            ShopIntent.OnAdReward -> onAdReward()
            is ShopIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
            ShopIntent.OnBuyCoinsSmallAmountClick -> onBuyCoinsSmallAmountClick()
            ShopIntent.OnBuyCoinsBigAmountClick -> onBuyCoinsBigAmount()
            ShopIntent.OnBuyAllCardsClick -> onBuyAllCardsClick()
            ShopIntent.OnDailyRewardCollectClick -> onDailyRewardCollectClick()
        }
    }

    private fun checkDailyStreak() {
        checkStreakJob?.cancel()
        checkStreakJob = checkDailyLoginStreakUseCase().onEach { result ->
            result.onSuccess { streakResult ->
                dailyStreakResult = streakResult
                showMenu()
            }
        }.launchIn(viewModelScope)
    }

    private fun onDailyRewardCollectClick() {
        hapticFeedback.vibrateLow()
        collectDailyStreakRewardUseCase().onEach { result ->
            result.onSuccess {
                dailyStreakResult?.let {
                    analytics.logDailyStreakCollected(it.streakDay, it.coinsReward)
                }
                notificationScheduler.scheduleStreakNotification()
                coinsPlayer.playDelayed()
                checkDailyStreak()
                loadCoins(animateCoins = true)
                checkShouldShowNotificationRequest()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadData(animateCoins: Boolean = false) {
        loadMenuItemsAndAd()
        loadCoins(animateCoins)
    }

    private fun onWatchAdClick() {
        hapticFeedback.vibrateLow()
        analytics.logAdShown(PLACEMENT)
        sendEffect(ShopEffect.ShowAd(allRewardedAds.shopCoinsAd))
    }

    private fun onAdReward() {
        showMenu(isAdAvailable = false)
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        analytics.logAdDismissed(PLACEMENT, wasRewardGranted)
        if (wasRewardGranted) {
            setLastShopAdShownTimestampUseCase().onEach { result ->
                result.onSuccess {
                    scheduleAdRewardNotificationUseCase().launchIn(viewModelScope)
                    checkShouldShowNotificationRequest()
                }.onFailure {
                    logE("Failed to save ad timestamp", it)
                }
            }.launchIn(viewModelScope)
            rewardCoinsForAd()
        }
        loadMenuItemsAndAd(wasRewardGranted)
    }

    private fun checkShouldShowNotificationRequest() {
        if (!notificationScheduler.hasNotificationPermission()) {
            sendEffect(ShopEffect.OpenEnableNotificationsScreen)
        }
    }

    private fun onBuyCoinsSmallAmountClick() {
        val product = productDetails.find { it.productId == BillingHandler.IAP_COINS_SMALL }
        product?.let {
            sendEffect(ShopEffect.LaunchBilling(billingHandler, it))
        } ?: run {
            sendEffect(ShopEffect.ShowPurchaseFailedError)
        }
    }

    private fun onBuyCoinsBigAmount() {
        val product = productDetails.find { it.productId == BillingHandler.IAP_COINS_BIG }
        product?.let {
            sendEffect(ShopEffect.LaunchBilling(billingHandler, it))
        } ?: run {
            sendEffect(ShopEffect.ShowPurchaseFailedError)
        }
    }

    private fun onBuyAllCardsClick() {
        val product = productDetails.find { it.productId == BillingHandler.IAP_UNLOCK_ALL_CARDS }
        product?.let {
            sendEffect(ShopEffect.LaunchBilling(billingHandler, it))
        } ?: run {
            sendEffect(ShopEffect.ShowPurchaseFailedError)
        }
    }

    private fun rewardCoins(coins: Long) {
        rewardCoinsForShopPurchaseUseCase(coins).onEach { result ->
            result.onSuccess {
                sendTotalCoinsScore()
                coinsPlayer.playDelayed()
                loadData(animateCoins = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun unlockAllCardPairs() {
        unlockAllCardPairsUseCase().onEach {
            it.onSuccess {
                coinsPlayer.playDelayed()
                loadData()
            }
        }.launchIn(viewModelScope)
    }

    private fun rewardCoinsForAd() {
        rewardCoinsForShopAdUseCase().onEach { result ->
            result.onSuccess {
                analytics.logAdRewardFromShop()
                sendTotalCoinsScore()
                coinsPlayer.playDelayed()
                loadCoins(animateCoins = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun sendTotalCoinsScore() {
        viewModelScope.launch {
            getTotalCoinsUseCase().first().onSuccess { totalCoins ->
                sendEffect(SendTotalCoinsScore(googlePlayGames, totalCoins))
            }
        }
    }

    private fun loadMenuItemsAndAd(wasRewardGranted: Boolean = false) {
        isShopAdCooldownOverUseCase().onEach { result ->
            result.onSuccess { isAdCooldownOver ->
                if (isAdCooldownOver) {
                    allRewardedAds.shopCoinsAd.loadAndNotify(wasRewardGranted) { isAvailable ->
                        showMenu(isAdAvailable = isAvailable)
                    }
                } else {
                    showMenu(isAdAvailable = false)
                }
            }.onFailure {
                showMenu(isAdAvailable = false)
            }
        }.launchIn(viewModelScope)
    }

    private fun loadCoins(animateCoins: Boolean) {
        loadCoinsJob?.cancel()
        loadCoinsJob = getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins, animateCoins = animateCoins) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadAdCoins() {
        calculateCoinsForShopAdUseCase().onEach { result ->
            result.onSuccess { coins ->
                showMenu(adCoins = coins)
            }
        }.launchIn(viewModelScope)
    }

    private fun showMenu(
        isAdAvailable: Boolean = currentAdAvailability(),
        adCoins: Long = currentCoinsToGrant()
    ) {
        val prices = priceByProductId
        val menu = mutableListOf<ShopMenuModel>()
        dailyStreakResult?.let { streak ->
            menu.add(
                ShopMenuModel.DailyReward(
                    isAvailable = streak.isRewardAvailable,
                    streakDay = streak.streakDay,
                    coinsToGrant = streak.coinsReward
                )
            )
        }
        menu.addAll(
            listOf(
                ShopMenuModel.WatchAd(isAdAvailable, adCoins),
                ShopMenuModel.BuyCoinsSmallAmount(
                    prices[BillingHandler.IAP_COINS_SMALL] ?: DEFAULT_PRICE,
                    SMALL_PURCHASE_COINS_REWARD
                ),
                ShopMenuModel.BuyCoinsBigAmount(
                    prices[BillingHandler.IAP_COINS_BIG] ?: DEFAULT_PRICE,
                    BIG_PURCHASE_COINS_REWARD
                ),
                ShopMenuModel.BuyAllCards(
                    prices[BillingHandler.IAP_UNLOCK_ALL_CARDS] ?: DEFAULT_PRICE
                )
            )
        )
        sendState { copy(menu = menu) }
    }

    private fun currentAdAvailability(): Boolean =
        (uiState.value.menu.find { it is ShopMenuModel.WatchAd } as? ShopMenuModel.WatchAd)
            ?.isAvailable ?: false

    private fun currentCoinsToGrant(): Long =
        (uiState.value.menu.find { it is ShopMenuModel.WatchAd } as? ShopMenuModel.WatchAd)
            ?.coinsToGrant ?: 0L

    private fun onProductsFetched(products: List<ProductDetails>) {
        productDetails = products
        showMenu()
    }

    private fun onPurchaseSuccessful(productId: String) {
        val offer = productDetails.firstOrNull { it.productId == productId }?.oneTimePurchaseOfferDetails
        analytics.logPurchaseCompleted(
            product = productId,
            priceMicros = offer?.priceAmountMicros ?: 0L,
            currencyCode = offer?.priceCurrencyCode.orEmpty()
        )
        when (productId) {
            in billingHandler.consumableProductIds -> {
                val coins = when (productId) {
                    BillingHandler.IAP_COINS_SMALL -> SMALL_PURCHASE_COINS_REWARD
                    BillingHandler.IAP_COINS_BIG -> BIG_PURCHASE_COINS_REWARD
                    else -> DEFAULT_COINS_AMOUNT
                }
                if (coins > DEFAULT_COINS_AMOUNT)
                    rewardCoins(coins)
            }

            in billingHandler.nonConsumableProductIds -> {
                unlockAllCardPairs()
            }
        }
    }

    private fun onPurchaseFailed() {
        analytics.logPurchaseFailed()
        sendEffect(ShopEffect.ShowPurchaseFailedError)
    }

    private fun onConnectionStatusChanged(isConnected: Boolean) {
        if (!isConnected) {
            sendEffect(ShopEffect.ShowConnectionError)
        }
    }

    companion object {
        const val DEFAULT_PRICE = "???"
        const val SMALL_PURCHASE_COINS_REWARD = 500L
        const val BIG_PURCHASE_COINS_REWARD = 3000L
        private const val DEFAULT_COINS_AMOUNT = 0L
        private const val PLACEMENT = "shop"
    }
}
