package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeNotificationScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ScheduleAdRewardNotificationUseCaseTest : AppTest() {

    @Inject
    lateinit var fakeNotificationScheduler: FakeNotificationScheduler

    private lateinit var sut: ScheduleAdRewardNotificationUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ScheduleAdRewardNotificationUseCase(
            testDispatcher,
            fakeNotificationScheduler
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when invoked then ad reward notification is scheduled`() = runTest {
        // when
        sut().first()

        // then
        assertTrue(fakeNotificationScheduler.adRewardNotificationScheduled)
    }
}
