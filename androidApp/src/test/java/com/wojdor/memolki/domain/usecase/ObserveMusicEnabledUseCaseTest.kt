package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ObserveMusicEnabledUseCaseTest : AppTest() {

    private val settingsRepository: SettingsRepository by inject()

    private lateinit var sut: ObserveMusicEnabledUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ObserveMusicEnabledUseCase(testDispatcher, settingsRepository)
    }

    @Test
    fun `when called then returns current music enabled value`() = runTest {
        // when
        sut().test {
            // then
            assertEquals(Result.success(true), awaitItem())
        }
    }

    @Test
    fun `when music is disabled then emits false`() = runTest {
        // given
        settingsRepository.setMusicEnabled(false)
        // when
        sut().test {
            // then
            assertEquals(Result.success(false), awaitItem())
        }
    }

    @Test
    fun `when value changes then emits new value`() = runTest {
        // when
        sut().test {
            // then
            assertEquals(Result.success(true), awaitItem())
            settingsRepository.setMusicEnabled(false)
            assertEquals(Result.success(false), awaitItem())
        }
    }
}
