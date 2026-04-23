package com.wojdor.memolki.ui.app

import com.wojdor.memolki.data.crypto.LocalEncryptorKeyStore
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.domain.usecase.GetTotalGamesPlayedUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.PrepareRecordingDataUseCase
import com.wojdor.memolki.domain.usecase.UnlockAllNewCardPairsIfPurchasedUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakePushNotificationProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.verifyOnce
import io.mockk.coEvery
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.provider.LocaleProvider
import com.wojdor.memolki.util.provider.PermissionProvider
import com.wojdor.memolki.util.provider.PushNotificationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class AppViewModelTest : AppTest() {

    private val analytics: Analytics by inject()

    private val unlockAllNewCardPairsIfPurchasedUseCase: UnlockAllNewCardPairsIfPurchasedUseCase by inject()

    private val localEncryptorKeyStore: LocalEncryptorKeyStore by inject()

    private val prepareRecordingDataUseCase: PrepareRecordingDataUseCase by inject()

    private val getTotalGamesPlayedUseCase: GetTotalGamesPlayedUseCase by inject()

    private val getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase by inject()

    private val localeProvider: LocaleProvider by inject()

    private val permissionProvider: PermissionProvider by inject()

    private val hasPlayedTodayDailyChallengeUseCase: HasPlayedTodayDailyChallengeUseCase by inject()

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private val pushNotificationProvider: PushNotificationProvider by inject()

    private val billingHandler: BillingHandler by inject()

    private lateinit var sut: AppViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = AppViewModel(
            analytics,
            unlockAllNewCardPairsIfPurchasedUseCase,
            localEncryptorKeyStore,
            prepareRecordingDataUseCase,
            getTotalGamesPlayedUseCase,
            getUnlockedCardPairsCountUseCase,
            hasPlayedTodayDailyChallengeUseCase,
            localeProvider,
            permissionProvider,
            pushNotificationProvider,
            billingHandler
        )
    }

    @Test
    fun `when onAppCreate is called then analytics session start is logged`() = runTest {
        // when
        sut.onAppCreate()
        testScheduler.advanceUntilIdle()

        // then
        verifyOnce { analytics.logAppSessionStart(any(), any()) }
    }

    @Test
    fun `when onAppCreate is called then user language is set`() = runTest {
        // when
        sut.onAppCreate()
        testScheduler.advanceUntilIdle()

        // then
        verifyOnce { analytics.setUserLanguage(any()) }
    }

    @Test
    fun `when onAppCreate is called then notification permission is set`() = runTest {
        // when
        sut.onAppCreate()
        testScheduler.advanceUntilIdle()

        // then
        verifyOnce { analytics.setNotificationPermission(any()) }
    }

    @Test
    fun `when onAppCreate is called then push notification topics are subscribed`() = runTest {
        // when
        sut.onAppCreate()
        testScheduler.advanceUntilIdle()

        // then
        assertTrue((pushNotificationProvider as FakePushNotificationProvider).topicsSubscribed)
    }

    @Test
    fun `when onAppCreate is called then billing connection is ensured`() = runTest {
        // when
        sut.onAppCreate()
        testScheduler.advanceUntilIdle()

        // then
        verifyOnce { billingHandler.ensureConnected() }
    }

    @Test
    fun `when onAppOpen is called then analytics logs app opened`() = runTest {
        // when
        sut.onAppOpen("test_notification", "test_shortcut")

        // then
        verifyOnce { analytics.logAppOpened("test_notification", "test_shortcut") }
    }

    @Test
    fun `when onAppOpen is called with null params then analytics logs app opened with nulls`() =
        runTest {
            // when
            sut.onAppOpen(null)

            // then
            verifyOnce { analytics.logAppOpened(null, null) }
        }

    @Test
    fun `when daily challenge not played today then hasPlayedTodayDailyChallenge returns false`() =
        runTest {
            // given
            val epochDay = fakeTimeProvider.currentLocalDate().toEpochDays()
            coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns false
            coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns null

            // when
            val result = sut.hasPlayedTodayDailyChallenge()

            // then
            assertFalse(result)
        }

    @Test
    fun `when daily challenge played today then hasPlayedTodayDailyChallenge returns true`() =
        runTest {
            // given
            val epochDay = fakeTimeProvider.currentLocalDate().toEpochDays()
            coEvery { dailyChallengeDao.hasPlayed(epochDay) } returns true
            coEvery { dailyChallengeDao.getLastPlayedEpochDay() } returns epochDay

            // when
            val result = sut.hasPlayedTodayDailyChallenge()

            // then
            assertTrue(result)
        }
}
