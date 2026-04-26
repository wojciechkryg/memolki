package com.wojdor.memolki.ui.app

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.dailyChallengeEntity
import com.wojdor.memolki.test.fake.FakeDailyChallengeDao
import com.wojdor.memolki.test.fake.FakePushNotificationProvider
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.verifyOnce
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.billing.BillingHandler
import com.wojdor.memolki.util.provider.PushNotificationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class AppViewModelTest : AppTest() {

    private val analytics: Analytics by inject()

    private val dailyChallengeDao: FakeDailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private val pushNotificationProvider: PushNotificationProvider by inject()

    private val billingHandler: BillingHandler by inject()

    private lateinit var sut: AppViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
            dailyChallengeDao.insertResult(dailyChallengeEntity(epochDay = epochDay))

            // when
            val result = sut.hasPlayedTodayDailyChallenge()

            // then
            assertTrue(result)
        }
}
