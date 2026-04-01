package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
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
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingStatusListener
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.GooglePlayGames
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ShopViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var coinsPlayer: CoinsPlayer

    @Inject
    lateinit var allRewardedAds: AllRewardedAds

    @Inject
    lateinit var billingHandler: BillingHandler

    @Inject
    lateinit var googlePlayGames: GooglePlayGames

    @Inject
    lateinit var isShopAdCooldownOverUseCase: IsShopAdCooldownOverUseCase

    @Inject
    lateinit var setLastShopAdShownTimestampUseCase: SetLastShopAdShownTimestampUseCase

    @Inject
    lateinit var getCoinsUseCase: GetCoinsUseCase

    @Inject
    lateinit var calculateCoinsForShopAdUseCase: CalculateCoinsForShopAdUseCase

    @Inject
    lateinit var rewardCoinsForShopAdUseCase: RewardCoinsForShopAdUseCase

    @Inject
    lateinit var rewardCoinsForShopPurchaseUseCase: RewardCoinsForShopPurchaseUseCase

    @Inject
    lateinit var unlockAllCardPairsUseCase: UnlockAllCardPairsUseCase

    @Inject
    lateinit var getTotalCoinsUseCase: GetTotalCoinsUseCase

    @Inject
    lateinit var scheduleAdRewardNotificationUseCase: ScheduleAdRewardNotificationUseCase

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    @Inject
    lateinit var checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase

    @Inject
    lateinit var collectDailyStreakRewardUseCase: CollectDailyStreakRewardUseCase

    @Inject
    lateinit var analytics: Analytics

    private lateinit var sut: ShopViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ShopViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            coinsPlayer,
            allRewardedAds,
            billingHandler,
            googlePlayGames,
            isShopAdCooldownOverUseCase,
            setLastShopAdShownTimestampUseCase,
            getCoinsUseCase,
            calculateCoinsForShopAdUseCase,
            rewardCoinsForShopAdUseCase,
            rewardCoinsForShopPurchaseUseCase,
            unlockAllCardPairsUseCase,
            getTotalCoinsUseCase,
            scheduleAdRewardNotificationUseCase,
            notificationScheduler,
            checkDailyLoginStreakUseCase,
            collectDailyStreakRewardUseCase
        )
    }


    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun testOnWatchAdClickIntent() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnWatchAdClick)

        // then
        assertTrue(sut.uiEffect.first() is ShopEffect.ShowAd)
    }

    @Test
    fun testOnAdRewardIntent() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdReward)

        // then
        sut.uiState.test {
            skipItems(2)
            val state = awaitItem()
            val watchAd = state.menu.filterIsInstance<ShopMenuModel.WatchAd>().first()
            assertEquals(false, watchAd.isAvailable)
        }
    }

    @Test
    fun `when purchase is completed then logPurchaseCompleted is called`() = runTest {
        // given
        val listenerSlot = slot<BillingStatusListener>()
        every { billingHandler.startConnection(capture(listenerSlot)) } answers {}
        sut = ShopViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            coinsPlayer,
            allRewardedAds,
            billingHandler,
            googlePlayGames,
            isShopAdCooldownOverUseCase,
            setLastShopAdShownTimestampUseCase,
            getCoinsUseCase,
            calculateCoinsForShopAdUseCase,
            rewardCoinsForShopAdUseCase,
            rewardCoinsForShopPurchaseUseCase,
            unlockAllCardPairsUseCase,
            getTotalCoinsUseCase,
            scheduleAdRewardNotificationUseCase,
            notificationScheduler,
            checkDailyLoginStreakUseCase,
            collectDailyStreakRewardUseCase
        )
        testScheduler.advanceUntilIdle()

        // when
        listenerSlot.captured.onPurchaseSuccessful("coins_small")
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logPurchaseCompleted("coins_small") }
    }

    @Test
    fun `when purchase fails then logPurchaseFailed is called`() = runTest {
        // given
        val listenerSlot = slot<BillingStatusListener>()
        every { billingHandler.startConnection(capture(listenerSlot)) } answers {}
        sut = ShopViewModel(
            savedStateHandle,
            analytics,
            hapticFeedback,
            coinsPlayer,
            allRewardedAds,
            billingHandler,
            googlePlayGames,
            isShopAdCooldownOverUseCase,
            setLastShopAdShownTimestampUseCase,
            getCoinsUseCase,
            calculateCoinsForShopAdUseCase,
            rewardCoinsForShopAdUseCase,
            rewardCoinsForShopPurchaseUseCase,
            unlockAllCardPairsUseCase,
            getTotalCoinsUseCase,
            scheduleAdRewardNotificationUseCase,
            notificationScheduler,
            checkDailyLoginStreakUseCase,
            collectDailyStreakRewardUseCase
        )
        testScheduler.advanceUntilIdle()

        // when
        listenerSlot.captured.onPurchaseFailed()
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logPurchaseFailed() }
    }

    @Test
    fun `when ad reward is earned then logAdRewardFromShop is called`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdRewardFromShop() }
    }
}
