package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ToggleSettingsUseCaseTest : AppTest() {

    private val settingsRepository: SettingsRepository by inject()

    private lateinit var sut: ToggleSettingsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when called with Music setting then it is toggled`() = runTest {
        // given
        val setting = SettingModel.Music(isEnabled = true)
        settingsRepository.setMusicEnabled(true)

        // when
        sut(setting).test {
            // then
            val expected = Result.success(setting.copy(isEnabled = false))
            assertEquals(expected, awaitItem())
            assertFalse(settingsRepository.getMusicEnabled().first())
            awaitComplete()
        }
    }

    @Test
    fun `when called with Sound setting then it is toggled`() = runTest {
        // given
        val setting = SettingModel.Sound(isEnabled = false)
        settingsRepository.setSoundEnabled(false)

        // when
        sut(setting).test {
            // then
            val expected = Result.success(setting.copy(isEnabled = true))
            assertEquals(expected, awaitItem())
            assertTrue(settingsRepository.getSoundEnabled().first())
            awaitComplete()
        }
    }

    @Test
    fun `when called with Vibration setting then it is toggled`() = runTest {
        // given
        val setting = SettingModel.Vibration(isEnabled = true)
        settingsRepository.setVibrationEnabled(true)

        // when
        sut(setting).test {
            // then
            val expected = Result.success(setting.copy(isEnabled = false))
            assertEquals(expected, awaitItem())
            assertFalse(settingsRepository.getVibrationEnabled().first())
            awaitComplete()
        }
    }
}
