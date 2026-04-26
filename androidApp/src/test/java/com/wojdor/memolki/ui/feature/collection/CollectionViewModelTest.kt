package com.wojdor.memolki.ui.feature.collection

import app.cash.turbine.test
import com.wojdor.memolki.data.local.datastore.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.test.fake.FakePermissionProvider
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.PermissionProvider
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CollectionViewModelTest : AppTest() {

    private val coinsPlayer: CoinsPlayer by inject()

    private val hapticFeedback: HapticFeedback by inject()

    private val allRewardedAds: AllRewardedAds by inject()

    private val userRepository: UserRepository by inject()

    private val unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource by inject()

    private val analytics: Analytics by inject()

    private val permissionProvider: PermissionProvider by inject()

    private lateinit var sut: CollectionViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when initial load is done then the state is updated with data`() = runTest {
        sut.uiState.test {
            // given
            userRepository.addCoins(123)
            skipItems(2)
            val state = awaitItem()

            // then
            assertEquals(123, state.coins)
            assertEquals(
                FakeAllCardPairsDataSource().getAllCardPairs()
                    .take(5)
                    .toModel(),
                state.collectionCardPairs
                    .take(5)
                    .filter { it is CollectionCardPairModel.Unlocked }
                    .map { (it as CollectionCardPairModel.Unlocked).cardPair }
            )
            assertEquals(5, state.unlockedCardPairsCount)
        }
    }

    @Test
    fun `when OnCardPairClick intent is send then the OpenCardPairDetailsScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // given
                val unlockedCardPair = CollectionCardPairModel.Unlocked(
                    FakeAllCardPairsDataSource().getAllCardPairs().first().toModel()
                )

                // when
                sut.sendIntent(CollectionIntent.OnCardPairClick(unlockedCardPair))

                // then
                assertEquals(
                    CollectionEffect.OpenCardPairDetailsScreen(unlockedCardPair.cardPair),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when OnShopClick intent is send then the OpenShopScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(CollectionIntent.OnShopClick)

                // then
                assertEquals(CollectionEffect.OpenShopScreen, awaitItem())
            }
        }

    @Test
    fun `when collection is loaded then logCollectionViewed is called once`() = runTest {
        // when
        sut.uiState.test {
            skipItems(2)
            awaitItem()

            // then
            verify(exactly = 1) { analytics.logCollectionViewed(any(), any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when card is unlocked with coins then logCardUnlockedWithCoins is called`() = runTest {
        // given
        userRepository.addCoins(1000)
        sut.uiState.test {
            skipItems(2)
            val state = awaitItem()
            val lockedWithCoins = state.collectionCardPairs
                .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                .first()

            // when
            sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
            cancelAndIgnoreRemainingEvents()
        }
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logCardUnlockedWithCoins(any(), any()) }
    }

    @Test
    fun `when shop button is clicked then logShopOpenedFromCollection is called`() = runTest {
        // when
        sut.sendIntent(CollectionIntent.OnShopClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logShopOpenedFromCollection() }
    }

    @Test
    fun `when OnShopClick intent is sent then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(CollectionIntent.OnShopClick)
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnCardPairClick intent is sent then haptic feedback is triggered`() = runTest {
        // given
        val unlockedCardPair = CollectionCardPairModel.Unlocked(
            FakeAllCardPairsDataSource().getAllCardPairs().first().toModel()
        )

        // when
        sut.sendIntent(CollectionIntent.OnCardPairClick(unlockedCardPair))
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnCardPairClick intent is sent then logCardPairDetailsViewed is called`() = runTest {
        // given
        val unlockedCardPair = CollectionCardPairModel.Unlocked(
            FakeAllCardPairsDataSource().getAllCardPairs().first().toModel()
        )

        // when
        sut.sendIntent(CollectionIntent.OnCardPairClick(unlockedCardPair))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logCardPairDetailsViewed() }
    }

    @Test
    fun `when OnUnlockWithCoinsClick with enough coins then coinsPlayer playDelayed is called`() =
        runTest {
            // given
            userRepository.addCoins(1000)
            sut.uiState.test {
                skipItems(2)
                val state = awaitItem()
                val lockedWithCoins = state.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .first()

                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            coVerify { coinsPlayer.playDelayed() }
        }

    @Test
    fun `when OnUnlockWithCoinsClick with enough coins then haptic feedback is triggered`() =
        runTest {
            // given
            userRepository.addCoins(1000)
            sut.uiState.test {
                skipItems(2)
                val state = awaitItem()
                val lockedWithCoins = state.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .first()

                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            verify { hapticFeedback.vibrateLow() }
        }

    @Test
    fun `when OnUnlockWithCoinsClick without enough coins then OpenShopScreen effect is sent`() =
        runTest {
            // given (default 0 coins)
            sut.uiState.test {
                skipItems(2)
                val state = awaitItem()
                val lockedWithCoins = state.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .first()

                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            sut.uiEffect.test {
                assertEquals(CollectionEffect.OpenShopScreen, awaitItem())
            }
        }

    @Test
    fun `when OnUnlockWithCoinsClick without enough coins then logInsufficientCoinsShown is called`() =
        runTest {
            // given (default 0 coins)
            sut.uiState.test {
                skipItems(2)
                val state = awaitItem()
                val lockedWithCoins = state.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .first()

                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logInsufficientCoinsShown(any(), any()) }
        }

    @Test
    fun `when OnUnlockWithCoinsClick without enough coins then logShopOpenedFromInsufficientCoins is called`() =
        runTest {
            // given (default 0 coins)
            sut.uiState.test {
                skipItems(2)
                val state = awaitItem()
                val lockedWithCoins = state.collectionCardPairs
                    .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
                    .first()

                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithCoinsClick(lockedWithCoins))
                cancelAndIgnoreRemainingEvents()
            }
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logShopOpenedFromInsufficientCoins() }
        }

    @Test
    fun `when OnUnlockWithAdClick intent is sent then haptic feedback is triggered`() = runTest {
        // when
        sut.sendIntent(CollectionIntent.OnUnlockWithAdClick(CollectionCardPairModel.LockedToUnlockWithAd))
        testScheduler.advanceUntilIdle()

        // then
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when OnUnlockWithAdClick intent is sent then logAdShown is called`() = runTest {
        // when
        sut.sendIntent(CollectionIntent.OnUnlockWithAdClick(CollectionCardPairModel.LockedToUnlockWithAd))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdShown("collection") }
    }

    @Test
    fun `when OnUnlockWithAdClick intent is sent then ShowAd effect is sent`() = runTest {
        sut.uiEffect.test {
            // when
            sut.sendIntent(CollectionIntent.OnUnlockWithAdClick(CollectionCardPairModel.LockedToUnlockWithAd))

            // then
            val effect = awaitItem()
            assertTrue(effect is CollectionEffect.ShowAd)
        }
    }

    @Test
    fun `when OnAdDismiss with reward granted then logAdDismissed is called with true`() =
        runTest {
            // given
            sut.uiState.test {
                skipItems(2)
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }

            // when
            sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = true))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdDismissed("collection", true) }
        }

    @Test
    fun `when OnAdDismiss without reward granted then logAdDismissed is called with false`() =
        runTest {
            // given
            sut.uiState.test {
                skipItems(2)
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }

            // when
            sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = false))
            testScheduler.advanceUntilIdle()

            // then
            verify { analytics.logAdDismissed("collection", false) }
        }

    @Test
    fun `when OnAdDismiss with reward granted and no notification permission then OpenEnableNotificationsScreen effect is sent`() =
        runTest {
            // given
            (permissionProvider as FakePermissionProvider).hasPermission = false
            sut.uiState.test {
                skipItems(2)
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }

            sut.uiEffect.test {
                // when
                sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = true))
                testScheduler.advanceUntilIdle()

                // then
                assertEquals(CollectionEffect.OpenEnableNotificationsScreen, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when OnAdDismiss without reward granted then no OpenEnableNotificationsScreen effect is sent`() =
        runTest {
            // given
            sut.uiState.test {
                skipItems(2)
                awaitItem()
                cancelAndIgnoreRemainingEvents()
            }

            // when
            sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = false))
            testScheduler.advanceUntilIdle()

            // then
            verify(exactly = 0) { analytics.logCardUnlockedWithAd(any(), any()) }
        }

    @Test
    fun `when initial load is done then collection contains both unlocked and locked card pairs`() =
        runTest {
            sut.uiState.test {
                // given
                skipItems(2)
                val state = awaitItem()

                // then
                val unlockedCount = state.collectionCardPairs
                    .count { it is CollectionCardPairModel.Unlocked }
                val lockedWithCoinsCount = state.collectionCardPairs
                    .count { it is CollectionCardPairModel.LockedToUnlockWithCoins }
                val lockedCount = state.collectionCardPairs
                    .count { it is CollectionCardPairModel.Locked }

                assertEquals(5, unlockedCount)
                assertTrue(lockedWithCoinsCount > 0)
                assertTrue(lockedCount > 0)
                assertTrue(state.collectionCardPairs.size > unlockedCount)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when OnUnlockWithAdClick intent is sent then ShowAd contains collectionCardPairAd`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(CollectionIntent.OnUnlockWithAdClick(CollectionCardPairModel.LockedToUnlockWithAd))

                // then
                val effect = awaitItem() as CollectionEffect.ShowAd
                assertEquals(allRewardedAds.collectionCardPairAd, effect.rewardedAd)
            }
        }

    @Test
    fun `when all cards unlocked then no locked to unlock with coins cards`() = runTest {
        // given
        listOf("watermelon", "mango", "peach", "pineapple", "blueberry").forEach {
            unlockedCardPairsLocalDataSource.addUnlockedCardPairId(it)
        }
        sut = get()

        // when
        sut.uiState.test {
            skipItems(1)
            val state = awaitItem()

            // then
            val lockedWithCoins = state.collectionCardPairs
                .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithCoins>()
            assertEquals(0, lockedWithCoins.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnAdReward then ad is not available`() = runTest {
        // given
        sut.uiState.test {
            skipItems(2)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // when
        sut.sendIntent(CollectionIntent.OnAdReward)
        testScheduler.advanceUntilIdle()

        // then
        val state = sut.uiState.value
        val lockedWithAd = state.collectionCardPairs
            .filterIsInstance<CollectionCardPairModel.LockedToUnlockWithAd>()
        assertTrue(lockedWithAd.isEmpty())
    }

    @Test
    fun `when OnAdDismiss with reward then logCardUnlockedWithAd is called`() = runTest {
        // given
        sut.uiState.test {
            skipItems(2)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // when
        sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logCardUnlockedWithAd(any(), any()) }
    }

    @Test
    fun `when OnAdDismiss with reward then logAdRewardFromCollection is called`() = runTest {
        // given
        sut.uiState.test {
            skipItems(2)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // when
        sut.sendIntent(CollectionIntent.OnAdDismiss(wasRewardGranted = true))
        testScheduler.advanceUntilIdle()

        // then
        verify { analytics.logAdRewardFromCollection() }
    }
}
