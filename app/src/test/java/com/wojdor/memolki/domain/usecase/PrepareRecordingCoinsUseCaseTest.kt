package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PrepareRecordingCoinsUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: PrepareRecordingCoinsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = PrepareRecordingCoinsUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when recording mode is enabled and coins are zero then add initial coins`() = runTest {
        @Suppress("KotlinConstantConditions")
        if (!RECORDING_MODE) return@runTest

        // when
        sut().first()

        // then
        val coins = userRepository.getCoins().first()
        assertEquals(473L, coins)
    }

    @Test
    fun `when recording mode is enabled and coins already exist then do not add coins`() = runTest {
        @Suppress("KotlinConstantConditions")
        if (!RECORDING_MODE) return@runTest

        // given
        userRepository.addCoins(100L)

        // when
        sut().first()

        // then
        val coins = userRepository.getCoins().first()
        assertEquals(100L, coins)
    }

    @Test
    fun `when executed then return success`() = runTest {
        // when
        val result = sut().first()

        // then
        assertTrue(result.isSuccess)
    }
}
