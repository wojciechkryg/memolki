package com.wojdor.memolki.domain.usecase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.coVerifyOnce
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SaveDailyChallengeUseCaseTest : AppTest() {

    @Inject
    lateinit var dailyChallengeDao: DailyChallengeDao

    @Inject
    lateinit var fakeTimeProvider: FakeTimeProvider

    @Inject
    lateinit var fakeNotificationScheduler: FakeNotificationScheduler

    @Inject
    lateinit var notificationLocalDataSource: NotificationLocalDataSource

    @Inject
    lateinit var encryptor: Encryptor

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var sut: SaveDailyChallengeUseCase

    @Before
    override fun setup() {
        super.setup()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns relaxedMockk()
        notificationRepository = NotificationRepository(
            encryptor,
            notificationLocalDataSource,
            fakeNotificationScheduler
        )
        sut = SaveDailyChallengeUseCase(
            testDispatcher,
            DailyChallengeRepository(dailyChallengeDao),
            notificationRepository,
            fakeTimeProvider
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when save daily challenge then insert entity with correct epoch day`() = runTest {
        // given
        val today = LocalDate.of(2026, 3, 26)
        fakeTimeProvider.mockCurrentDate = today
        val model = DailyChallengeModel(
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = listOf(listOf(1, 2), listOf(3, 4))
        )

        // when
        val result = sut(model).first()

        // then
        assertEquals(Result.success(Unit), result)
        val expectedEntity = DailyChallengeEntity(
            epochDay = today.toEpochDay(),
            mistakeCount = 3,
            starCount = 2,
            timeMillis = 45000L,
            cardFlipCounts = "1,2;3,4"
        )
        coVerifyOnce { dailyChallengeDao.insertResult(expectedEntity) }
    }

    @Test
    fun `when save daily challenge then schedule next daily challenge notification`() = runTest {
        // given
        fakeTimeProvider.mockCurrentDate = LocalDate.of(2026, 3, 26)

        // when
        sut(DailyChallengeModel()).first()

        // then
        val expectedTimestamp = fakeNotificationScheduler.nextDailyChallengeNotificationTimestamp
        assertEquals(
            expectedTimestamp,
            fakeNotificationScheduler.dailyChallengeNotificationTimestamp
        )
        assertEquals(
            expectedTimestamp,
            notificationRepository.getNextDailyChallengeNotificationTimestamp()
        )
    }
}
