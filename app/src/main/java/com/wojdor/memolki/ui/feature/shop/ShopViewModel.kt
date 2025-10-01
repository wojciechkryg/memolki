package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCase
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
    private val rewardCoinsForShopAdUseCase: RewardCoinsForShopAdUseCase
) : MviViewModel<ShopIntent, ShopState>(
    savedStateHandle,
    ShopState()
) {

    init {
        loadData()
    }

    override fun onIntent(intent: ShopIntent) {
        when (intent) {
            ShopIntent.OnRewardCoinsWithAdClick -> onUnlockWithAdClick()
            ShopIntent.OnAdReward -> onAdReward()
            is ShopIntent.OnAdDismiss -> onAdDismiss(intent.wasRewardGranted)
        }
    }

    private fun loadData(animateCoins: Boolean = false) {
        loadMenuItemsAndAd()
        loadCoins(animateCoins)
    }

    private fun onUnlockWithAdClick() {
        hapticFeedback.vibrateLow()
        sendEffect(ShopEffect.ShowAd(allRewardedAds.shopCoinsAd))
    }

    private fun onAdReward() {
        loadMenu(isAdAvailable = false)
        allRewardedAds.shopCoinsAd.load()
    }

    private fun onAdDismiss(wasRewardGranted: Boolean) {
        if (wasRewardGranted) {
            rewardCoinsForAd()
        }
        loadMenuItemsAndAd(wasRewardGranted)
    }

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
        if (allRewardedAds.collectionCardPairAd.isLoaded && !wasRewardGranted) {
            loadMenu(isAdAvailable = true)
        } else {
            loadMenu(isAdAvailable = false)
            allRewardedAds.collectionCardPairAd.load(
                onLoaded = {
                    if (!wasRewardGranted) {
                        loadMenu(isAdAvailable = true)
                    }
                },
                onFailed = {
                    loadMenu(isAdAvailable = false)
                }
            )
        }
    }

    private fun loadMenu(isAdAvailable: Boolean) {
        // TODO: reload menu items
    }

    private fun loadCoins(animateCoins: Boolean) {
        getCoinsUseCase().onEach {
            it.onSuccess { coins ->
                sendState { copy(coins = coins, animateCoins = animateCoins) }
            }
        }.launchIn(viewModelScope)
    }

}

private const val COINS_SOUND_DELAY = 300L
