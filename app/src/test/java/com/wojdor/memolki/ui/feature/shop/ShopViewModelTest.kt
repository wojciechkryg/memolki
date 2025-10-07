package com.wojdor.memolki.ui.feature.shop

import app.cash.turbine.test
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopAdUseCase
import com.wojdor.memolki.domain.usecase.RewardCoinsForShopPurchaseUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.relaxedMockk
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopViewModelTest : AppTest() {

    private lateinit var userLocalDataSource: UserLocalDataSource
    private lateinit var userRepository: UserRepository
    private lateinit var cardRepository: CardRepository
    private lateinit var getCoinsUseCase: GetCoinsUseCase
    private lateinit var rewardCoinsForShopAdUseCase: RewardCoinsForShopAdUseCase
    private lateinit var rewardCoinsForShopPurchaseUseCase: RewardCoinsForShopPurchaseUseCase
    private lateinit var getUnlockedCardPairsUseCase: GetUnlockedCardPairsUseCase
    private lateinit var getLevelsUseCase: GetLevelsUseCase
    private val hapticFeedback: HapticFeedback = relaxedMockk()
    private val coinsPlayer: CoinsPlayer = relaxedMockk()
    private val allRewardedAds: AllRewardedAds = relaxedMockk()
    private val encryptor: Encryptor = relaxedMockk()
    private lateinit var viewModel: ShopViewModel

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        userLocalDataSource = UserLocalDataSource(dataStore)
        userRepository = UserRepository(encryptor, userLocalDataSource)
        cardRepository = CardRepository(
            MockAllCardPairsDataSource,
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        )
        getUnlockedCardPairsUseCase = GetUnlockedCardPairsUseCase(testDispatcher, cardRepository)
        getLevelsUseCase = GetLevelsUseCase(
            testDispatcher,
            GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository)
        )
        getCoinsUseCase = GetCoinsUseCase(testDispatcher, userRepository)
        rewardCoinsForShopAdUseCase =
            RewardCoinsForShopAdUseCase(testDispatcher, userRepository, getLevelsUseCase)
        rewardCoinsForShopPurchaseUseCase =
            RewardCoinsForShopPurchaseUseCase(testDispatcher, userRepository)
        viewModel = ShopViewModel(
            savedStateHandle,
            hapticFeedback,
            coinsPlayer,
            allRewardedAds,
            getCoinsUseCase,
            rewardCoinsForShopAdUseCase,
            rewardCoinsForShopPurchaseUseCase
        )
    }

    @Test
    fun `When OnWatchAdClick intent is sent then should show ad`() = runTest {
        // when
        viewModel.sendIntent(ShopIntent.OnWatchAdClick)

        // then
        assertEquals(ShopEffect.ShowAd(allRewardedAds.shopCoinsAd), viewModel.uiEffect.first())
    }

    @Test
    fun `When OnAdReward intent is sent then should hide ad`() = runTest {
        // when
        viewModel.sendIntent(ShopIntent.OnAdReward)

        // then
        val menu = viewModel.uiState.first().menu
        assertEquals(
            false,
            (menu.first() as com.wojdor.memolki.domain.model.ShopMenuModel.WatchAd).isAdAvailable
        )
    }
}
