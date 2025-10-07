package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopPurchaseUseCase
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.ui.base.MviViewModel
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hapticFeedback: HapticFeedback,
    private val coinsPlayer: CoinsPlayer,
    private val allRewardedAds: AllRewardedAds,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val rewardCoinsForShopAdUseCase: RewardCoinsForShopAdUseCase,
    private val rewardCoinsForShopPurchaseUseCase: RewardCoinsForShopPurchaseUseCase
) : MviViewModel<ShopIntent, ShopState>(
    savedStateHandle,
    ShopState()
) {

    init {
        loadData()
    }

    override fun onIntent(intent: ShopIntent) {
        when (intent) {
            ShopIntent.OnWatchAdClick -> onWatchAdClick()
            ShopIntent.OnAdReward -> onAdReward()
            is ShopIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
            ShopIntent.OnBuyCoinsSmallAmountClick -> onBuyCoinsClick(SMALL_PURCHASE_COINS_REWARD)
            ShopIntent.OnBuyCoinsBigAmountClick -> onBuyCoinsClick(BIG_PURCHASE_COINS_REWARD)
            ShopIntent.OnBuyAllCardsClick -> onBuyAllCardsClick()
        }
    }

    private fun loadData(animateCoins: Boolean = false) {
        loadMenuItemsAndAd()
        loadCoins(animateCoins)
    }

    private fun onWatchAdClick() {
        hapticFeedback.vibrateLow()
        sendEffect(ShopEffect.ShowAd(allRewardedAds.shopCoinsAd))
    }

    private fun onAdReward() {
        showMenu(isAdAvailable = false)
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        if (wasRewardGranted) {
            rewardCoinsForAd()
        }
        loadMenuItemsAndAd(wasRewardGranted)
    }

    private fun onBuyCoinsClick(coins: Long) {
        rewardCoinsForShopPurchaseUseCase(coins).onEach { result ->
            result.onSuccess {
                delay(COINS_SOUND_DELAY)
                coinsPlayer.play()
                loadData(animateCoins = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun onBuyAllCardsClick() = Unit // TODO: replace with purchase flow

    private fun rewardCoinsForAd() {
        rewardCoinsForShopAdUseCase().onEach { result ->
            result.onSuccess {
                delay(COINS_SOUND_DELAY)
                coinsPlayer.play()
                loadData(animateCoins = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun loadMenuItemsAndAd(wasRewardGranted: Boolean = false) {
        if (allRewardedAds.shopCoinsAd.isLoaded && !wasRewardGranted) {
            showMenu(isAdAvailable = true)
        } else {
            showMenu(isAdAvailable = false)
            allRewardedAds.shopCoinsAd.load(
                onLoaded = {
                    if (!wasRewardGranted) {
                        showMenu(isAdAvailable = true)
                    }
                },
                onFailed = {
                    showMenu(isAdAvailable = false)
                }
            )
        }
    }

    private fun loadCoins(animateCoins: Boolean) {
        getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins, animateCoins = animateCoins) }
            }
        }.launchIn(viewModelScope)
    }

    private fun showMenu(isAdAvailable: Boolean) {
        sendState {
            copy(
                menu = listOf(
                    ShopMenuModel.WatchAd(isAdAvailable),
                    ShopMenuModel.BuyCoinsSmallAmount,
                    ShopMenuModel.BuyCoinsBigAmount,
                    ShopMenuModel.BuyAllCards
                )
            )
        }
    }
}

private const val COINS_SOUND_DELAY = 300L
private const val SMALL_PURCHASE_COINS_REWARD = 500L
private const val BIG_PURCHASE_COINS_REWARD = 3000L
