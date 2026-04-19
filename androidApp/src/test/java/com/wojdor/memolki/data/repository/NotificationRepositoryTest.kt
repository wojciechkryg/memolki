package com.wojdor.memolki.data.repository

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class NotificationRepositoryTest : AppTest() {

    private val notificationLocalDataSource: NotificationLocalDataSource by inject()

    private val encryptor: Encryptor by inject()

    private val fakeNotificationScheduler: FakeNotificationScheduler by inject()

    private lateinit var sut: NotificationRepository

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        sut = NotificationRepository(
            encryptor,
            notificationLocalDataSource,
            fakeNotificationScheduler
        )
    }

    @Test
    fun `when getLastShownTimestamp with no data then returns default zero`() = runTest {
        // when
        val result = sut.getLastShownTimestamp()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when getLastShownTimestamp with stored value then returns decrypted value`() = runTest {
        // given
        val expected = 1234567890L
        notificationLocalDataSource.setEncryptedLastShownTimestamp(encryptor.encrypt(expected))

        // when
        val result = sut.getLastShownTimestamp()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when setLastShownTimestamp then value can be retrieved`() = runTest {
        // given
        val timestamp = 9876543210L

        // when
        sut.setLastShownTimestamp(timestamp)

        // then
        val result = sut.getLastShownTimestamp()
        assertEquals(timestamp, result)
    }

    @Test
    fun `when stored value is corrupted then returns default zero`() = runTest {
        // given
        notificationLocalDataSource.setEncryptedLastShownTimestamp("corrupted_data")

        // when
        val result = sut.getLastShownTimestamp()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when getNextDailyChallengeNotificationTimestamp with no data then returns default zero`() =
        runTest {
            // when
            val result = sut.getNextDailyChallengeNotificationTimestamp()

            // then
            assertEquals(0L, result)
        }

    @Test
    fun `when setNextDailyChallengeNotificationTimestamp then value can be retrieved`() = runTest {
        // given
        val timestamp = 1_700_000_000_000L

        // when
        sut.setNextDailyChallengeNotificationTimestamp(timestamp)

        // then
        val result = sut.getNextDailyChallengeNotificationTimestamp()
        assertEquals(timestamp, result)
    }

    @Test
    fun `when stored next daily challenge timestamp is corrupted then returns default zero`() =
        runTest {
            // given
            notificationLocalDataSource
                .setEncryptedNextDailyChallengeNotificationTimestamp("corrupted_data")

            // when
            val result = sut.getNextDailyChallengeNotificationTimestamp()

            // then
            assertEquals(0L, result)
        }

    @Test
    fun `when scheduleNextDailyChallengeNotification then persists and schedules next timestamp`() =
        runTest {
            // when
            sut.scheduleNextDailyChallengeNotification()

            // then
            val expectedTimestamp = fakeNotificationScheduler.nextDailyChallengeNotificationTimestamp
            assertEquals(
                expectedTimestamp,
                fakeNotificationScheduler.dailyChallengeNotificationTimestamp
            )
            assertEquals(expectedTimestamp, sut.getNextDailyChallengeNotificationTimestamp())
        }
}
