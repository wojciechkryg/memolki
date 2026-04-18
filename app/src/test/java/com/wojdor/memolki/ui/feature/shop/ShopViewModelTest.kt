package com.wojdor.memolki.ui.feature.shop

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
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
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingStatusListener
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.playgames.GooglePlayGames
import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Inject
    lateinit var permissionProvider: PermissionProvider

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
        testScheduler.advanceUntilIdle()

        // then
        val watchAd = sut.uiState.value.menu
            .filterIsInstance<ShopMenuModel.WatchAd>().first()
        assertFalse(watchAd.isAvailable)
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
        verify { analytics.logPurchaseCompleted(product = "coins_small", priceMicros = any(), currencyCode = any()) }
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

    @Test
    fun `when OnWatchAdClick then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnWatchAdClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnWatchAdClick then logAdShown is called`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnWatchAdClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdShown("shop") }
    }

    @Test
    fun `when OnWatchAdClick then ShowAd effect is sent`() = runTest {
        sut.uiEffect.test {
            // when
            sut.sendIntent(ShopIntent.OnWatchAdClick)

            // then
            assertTrue(awaitItem() is ShopEffect.ShowAd)
        }
    }

    @Test
    fun `when OnAdReward then WatchAd menu item is not available`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdReward)
        testScheduler.advanceUntilIdle()

        // then
        val watchAd = sut.uiState.value.menu
            .filterIsInstance<ShopMenuModel.WatchAd>().first()
        assertFalse(watchAd.isAvailable)
    }

    @Test
    fun `when OnAdDismiss with reward granted then logAdDismissed is called with true`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdDismissed("shop", true) }
    }

    @Test
    fun `when OnAdDismiss with reward granted then logAdRewardFromShop is called`() =
        runTest {
            // when
            sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = true))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdRewardFromShop() }
        }

    @Test
    fun `when OnAdDismiss with reward granted then coins are rewarded`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        coVerify { coinsPlayer.playDelayed() }
    }

    @Test
    fun `when OnAdDismiss without reward then logAdDismissed is called with false`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = false))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdDismissed("shop", false) }
    }

    @Test
    fun `when OnAdDismiss without reward then logAdRewardFromShop is not called`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = false))
        testScheduler.advanceUntilIdle()

        // then
        verify(exactly = 0) { analytics.logAdRewardFromShop() }
    }

    @Test
    fun `when OnBuyCoinsSmallAmountClick without product then ShowPurchaseFailedError is sent`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(ShopIntent.OnBuyCoinsSmallAmountClick)

                // then
                assertEquals(ShopEffect.ShowPurchaseFailedError, awaitItem())
            }
        }

    @Test
    fun `when OnBuyCoinsBigAmountClick without product then ShowPurchaseFailedError is sent`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(ShopIntent.OnBuyCoinsBigAmountClick)

                // then
                assertEquals(ShopEffect.ShowPurchaseFailedError, awaitItem())
            }
        }

    @Test
    fun `when OnBuyAllCardsClick without product then ShowPurchaseFailedError is sent`() = runTest {
        sut.uiEffect.test {
            // when
            sut.sendIntent(ShopIntent.OnBuyAllCardsClick)

            // then
            assertEquals(ShopEffect.ShowPurchaseFailedError, awaitItem())
        }
    }

    @Test
    fun `when OnDailyRewardCollectClick then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnDailyRewardCollectClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnDailyRewardCollectClick then coins player is played`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnDailyRewardCollectClick)
        testScheduler.advanceUntilIdle()

        // then
        coVerify { coinsPlayer.playDelayed() }
    }

    @Test
    fun `when OnDailyRewardCollectClick then streak notification is scheduled`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnDailyRewardCollectClick)
        testScheduler.advanceUntilIdle()

        // then
        assertTrue((notificationScheduler as FakeNotificationScheduler).streakNotificationScheduled)
    }

    private fun createSutWithCapturedBillingListener(): BillingStatusListener {
        val listenerSlot = slot<BillingStatusListener>()
        every { billingHandler.startConnection(capture(listenerSlot)) } answers {}
        every { billingHandler.consumableProductIds } returns setOf(
            BillingHandler.IAP_COINS_SMALL,
            BillingHandler.IAP_COINS_BIG
        )
        every { billingHandler.nonConsumableProductIds } returns setOf(
            BillingHandler.IAP_UNLOCK_ALL_CARDS
        )
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
        return listenerSlot.captured
    }

    @Test
    fun `when onPurchaseSuccessful with coins_small then purchase completed is logged`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_COINS_SMALL)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_COINS_SMALL, priceMicros = any(), currencyCode = any()) }
        }

    @Test
    fun `when onPurchaseSuccessful with coins_small then coins are animated`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_COINS_SMALL)
            testScheduler.advanceUntilIdle()

            // then
            assertTrue(sut.uiState.value.animateCoins)
        }

    @Test
    fun `when onPurchaseSuccessful with coins_big then purchase completed is logged`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_COINS_BIG)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_COINS_BIG, priceMicros = any(), currencyCode = any()) }
        }

    @Test
    fun `when onPurchaseSuccessful with unlock_all_cards then purchase completed is logged`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_UNLOCK_ALL_CARDS)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_UNLOCK_ALL_CARDS, priceMicros = any(), currencyCode = any()) }
        }

    @Test
    fun `when onPurchaseFailed then ShowPurchaseFailedError effect is sent`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()

        sut.uiEffect.test {
            // when
            listener.onPurchaseFailed()
            testScheduler.advanceUntilIdle()

            // then
            assertEquals(ShopEffect.ShowPurchaseFailedError, awaitItem())
        }
    }

    @Test
    fun `when onPurchaseFailed then logPurchaseFailed is called`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()

        // when
        listener.onPurchaseFailed()
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logPurchaseFailed() }
    }

    @Test
    fun `when onConnectionStatusChanged with false then ShowConnectionError effect is sent`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                listener.onConnectionStatusChanged(false)
                testScheduler.advanceUntilIdle()

                // then
                assertEquals(ShopEffect.ShowConnectionError, awaitItem())
            }
        }

    @Test
    fun `when onConnectionStatusChanged with true then no ShowConnectionError effect is sent`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                listener.onConnectionStatusChanged(true)
                testScheduler.advanceUntilIdle()

                // then
                expectNoEvents()
            }
        }

    @Test
    fun `when OnBuyCoinsSmallAmountClick with product then LaunchBilling effect is sent`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()
            val mockProduct = mockk<ProductDetails> {
                every { productId } returns BillingHandler.IAP_COINS_SMALL
                every { oneTimePurchaseOfferDetails } returns mockk {
                    every { formattedPrice } returns "$0.99"
                }
            }
            listener.onProductsFetched(listOf(mockProduct))
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                // when
                sut.sendIntent(ShopIntent.OnBuyCoinsSmallAmountClick)

                // then
                val effect = awaitItem()
                assertTrue(effect is ShopEffect.LaunchBilling)
            }
        }

    @Test
    fun `when OnBuyCoinsBigAmountClick with product then LaunchBilling effect is sent`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()
        val mockProduct = mockk<ProductDetails> {
            every { productId } returns BillingHandler.IAP_COINS_BIG
            every { oneTimePurchaseOfferDetails } returns mockk {
                every { formattedPrice } returns "$4.99"
            }
        }
        listener.onProductsFetched(listOf(mockProduct))
        testScheduler.advanceUntilIdle()

        sut.uiEffect.test {
            // when
            sut.sendIntent(ShopIntent.OnBuyCoinsBigAmountClick)

            // then
            val effect = awaitItem()
            assertTrue(effect is ShopEffect.LaunchBilling)
        }
    }

    @Test
    fun `when OnBuyAllCardsClick with product then LaunchBilling effect is sent`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()
        val mockProduct = mockk<ProductDetails> {
            every { productId } returns BillingHandler.IAP_UNLOCK_ALL_CARDS
            every { oneTimePurchaseOfferDetails } returns mockk {
                every { formattedPrice } returns "$9.99"
            }
        }
        listener.onProductsFetched(listOf(mockProduct))
        testScheduler.advanceUntilIdle()

        sut.uiEffect.test {
            // when
            sut.sendIntent(ShopIntent.OnBuyAllCardsClick)

            // then
            val effect = awaitItem()
            assertTrue(effect is ShopEffect.LaunchBilling)
        }
    }

    @Test
    fun `when OnAdDismiss with reward and no notification permission then OpenEnableNotificationsScreen is sent`() =
        runTest {
            // given
            (permissionProvider as FakePermissionProvider).hasPermission = false

            sut.uiEffect.test {
                // when
                sut.sendIntent(ShopIntent.OnAdDismiss(wasRewardGranted = true))
                testScheduler.advanceUntilIdle()

                // then
                assertTrue(awaitItem() is ShopEffect.OpenEnableNotificationsScreen)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when onPurchaseSuccessful with unknown consumable then coins are not rewarded`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            every { billingHandler.consumableProductIds } returns setOf(
                BillingHandler.IAP_COINS_SMALL,
                BillingHandler.IAP_COINS_BIG,
                "unknown_consumable"
            )
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful("unknown_consumable")
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = "unknown_consumable", priceMicros = any(), currencyCode = any()) }
            coVerify(exactly = 0) { coinsPlayer.playDelayed() }
        }

    @Test
    fun `when onProductsFetched then menu contains prices from products`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()

        val mockProductSmall = mockk<ProductDetails> {
            every { productId } returns BillingHandler.IAP_COINS_SMALL
            every { oneTimePurchaseOfferDetails } returns mockk {
                every { formattedPrice } returns "$0.99"
            }
        }
        val mockProductBig = mockk<ProductDetails> {
            every { productId } returns BillingHandler.IAP_COINS_BIG
            every { oneTimePurchaseOfferDetails } returns mockk {
                every { formattedPrice } returns "$4.99"
            }
        }
        val mockProductUnlockAll = mockk<ProductDetails> {
            every { productId } returns BillingHandler.IAP_UNLOCK_ALL_CARDS
            every { oneTimePurchaseOfferDetails } returns mockk {
                every { formattedPrice } returns "$9.99"
            }
        }

        // when
        listener.onProductsFetched(listOf(mockProductSmall, mockProductBig, mockProductUnlockAll))
        testScheduler.advanceUntilIdle()

        // then
        val menu = sut.uiState.value.menu
        val buySmall = menu.filterIsInstance<ShopMenuModel.BuyCoinsSmallAmount>().first()
        val buyBig = menu.filterIsInstance<ShopMenuModel.BuyCoinsBigAmount>().first()
        val buyAll = menu.filterIsInstance<ShopMenuModel.BuyAllCards>().first()
        assertEquals("$0.99", buySmall.formattedPrice)
        assertEquals("$4.99", buyBig.formattedPrice)
        assertEquals("$9.99", buyAll.formattedPrice)
    }
}
