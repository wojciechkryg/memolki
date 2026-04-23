package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ScheduleAdRewardNotificationUseCaseTest : AppTest() {

    private val fakeNotificationScheduler: FakeNotificationScheduler by inject()

    private lateinit var sut: ScheduleAdRewardNotificationUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when invoked then ad reward notification is scheduled`() = runTest {
        // when
        sut().first()

        // then
        assertTrue(fakeNotificationScheduler.adRewardNotificationScheduled)
    }
}
