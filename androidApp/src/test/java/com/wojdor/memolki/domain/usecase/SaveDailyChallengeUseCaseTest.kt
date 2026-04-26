package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.coVerifyOnce
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import com.wojdor.memolki.test.fake.FakeTimeProvider
import com.wojdor.memolki.test.relaxedMockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.datetime.LocalDate
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SaveDailyChallengeUseCaseTest : AppTest() {

    private val dailyChallengeDao: DailyChallengeDao by inject()

    private val fakeTimeProvider: FakeTimeProvider by inject()

    private val fakeNotificationScheduler: FakeNotificationScheduler by inject()

    private val notificationLocalDataSource: NotificationLocalDataSource by inject()

    private val encryptor: Encryptor by inject()

    private lateinit var notificationRepository: NotificationRepository
    private lateinit var sut: SaveDailyChallengeUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        notificationRepository = NotificationRepository(
            encryptor,
            notificationLocalDataSource,
            fakeNotificationScheduler
        )
        sut = get()
    }

    @Test
    fun `when save daily challenge then insert entity with correct epoch day`() = runTest {
        // given
        val today = LocalDate(2026, 3, 26)
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
            epochDay = today.toEpochDays(),
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
        fakeTimeProvider.mockCurrentDate = LocalDate(2026, 3, 26)

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
