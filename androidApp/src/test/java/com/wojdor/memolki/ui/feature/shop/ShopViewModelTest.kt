package com.wojdor.memolki.ui.feature.shop

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.ShopMenuModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.test.fake.FakeBillingHandler
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.billing.BillingProduct
import com.wojdor.memolki.util.billing.BillingStatusListener
import com.wojdor.memolki.test.fake.FakeCoinsPlayer
import com.wojdor.memolki.test.fake.FakeHapticFeedback
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.coVerify
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ShopViewModelTest : AppTest() {

    private val hapticFeedback: FakeHapticFeedback by inject()

    private val coinsPlayer: FakeCoinsPlayer by inject()

    private val billingHandler: FakeBillingHandler by inject()

    private val notificationScheduler: NotificationScheduler by inject()

    private val analytics: Analytics by inject()

    private val permissionProvider: PermissionProvider by inject()

    private lateinit var sut: ShopViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
        sut = get()
        testScheduler.advanceUntilIdle()
        val listener = billingHandler.capturedListener!!
        listener.onProductsFetched(listOf(billingProduct("coins_small", priceMicros = 990_000L, currency = "USD")))
        testScheduler.advanceUntilIdle()

        // when
        listener.onPurchaseSuccessful("coins_small")
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logPurchaseCompleted(product = "coins_small", priceMicros = 990_000L, currencyCode = "USD") }
    }

    @Test
    fun `when purchase fails then logPurchaseFailed is called`() = runTest {
        // given
        sut = get()
        testScheduler.advanceUntilIdle()

        // when
        billingHandler.capturedListener!!.onPurchaseFailed()
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
        assertEquals(1, hapticFeedback.vibrateLowCount)
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
        assertEquals(1, coinsPlayer.playDelayedCount)
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
        assertEquals(1, hapticFeedback.vibrateLowCount)
    }

    @Test
    fun `when OnDailyRewardCollectClick then coins player is played`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnDailyRewardCollectClick)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(1, coinsPlayer.playDelayedCount)
    }

    @Test
    fun `when OnDailyRewardCollectClick then streak notification is scheduled`() = runTest {
        // when
        sut.sendIntent(ShopIntent.OnDailyRewardCollectClick)
        testScheduler.advanceUntilIdle()

        // then
        assertTrue((notificationScheduler as FakeNotificationScheduler).streakNotificationScheduled)
    }

    private fun billingProduct(
        id: String,
        priceMicros: Long = 990_000L,
        currency: String = "USD",
        formattedPriceText: String = "$0.99"
    ) = BillingProduct(
        id = id,
        formattedPrice = formattedPriceText,
        priceMicros = priceMicros,
        currencyCode = currency
    )

    private fun createSutWithCapturedBillingListener(): BillingStatusListener {
        sut = get()
        return billingHandler.capturedListener!!
    }

    @Test
    fun `when onPurchaseSuccessful with coins_small then purchase completed is logged`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()
            listener.onProductsFetched(listOf(billingProduct(BillingHandler.IAP_COINS_SMALL, priceMicros = 990_000L, currency = "USD")))
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_COINS_SMALL)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_COINS_SMALL, priceMicros = 990_000L, currencyCode = "USD") }
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
            listener.onProductsFetched(listOf(billingProduct(BillingHandler.IAP_COINS_BIG, priceMicros = 4_990_000L, currency = "USD")))
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_COINS_BIG)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_COINS_BIG, priceMicros = 4_990_000L, currencyCode = "USD") }
        }

    @Test
    fun `when onPurchaseSuccessful with unlock_all_cards then purchase completed is logged`() =
        runTest {
            // given
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()
            listener.onProductsFetched(listOf(billingProduct(BillingHandler.IAP_UNLOCK_ALL_CARDS, priceMicros = 14_990_000L, currency = "USD")))
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful(BillingHandler.IAP_UNLOCK_ALL_CARDS)
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logPurchaseCompleted(product = BillingHandler.IAP_UNLOCK_ALL_CARDS, priceMicros = 14_990_000L, currencyCode = "USD") }
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
            val mockProduct = billingProduct(BillingHandler.IAP_COINS_SMALL, formattedPriceText = "$0.99")
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
        val mockProduct = billingProduct(BillingHandler.IAP_COINS_BIG, formattedPriceText = "$4.99")
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
        val mockProduct = billingProduct(BillingHandler.IAP_UNLOCK_ALL_CARDS, formattedPriceText = "$9.99")
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
            billingHandler.consumableProductIds = setOf(
                BillingHandler.IAP_COINS_SMALL,
                BillingHandler.IAP_COINS_BIG,
                "unknown_consumable"
            )
            val listener = createSutWithCapturedBillingListener()
            testScheduler.advanceUntilIdle()

            // when
            listener.onPurchaseSuccessful("unknown_consumable")
            testScheduler.advanceUntilIdle()

            // then
            verify(exactly = 0) { analytics.logPurchaseCompleted(any(), any(), any()) }
            assertEquals(0, coinsPlayer.playDelayedCount)
        }

    @Test
    fun `when onProductsFetched then menu contains prices from products`() = runTest {
        // given
        val listener = createSutWithCapturedBillingListener()
        testScheduler.advanceUntilIdle()

        val mockProductSmall = billingProduct(BillingHandler.IAP_COINS_SMALL, formattedPriceText = "$0.99")
        val mockProductBig = billingProduct(BillingHandler.IAP_COINS_BIG, formattedPriceText = "$4.99")
        val mockProductUnlockAll = billingProduct(BillingHandler.IAP_UNLOCK_ALL_CARDS, formattedPriceText = "$9.99")

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
