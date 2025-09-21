package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleSettingsUseCaseTest : AppTest() {

    private val settingsRepository = SettingsRepository(SettingsLocalDataSource(MockDataStore()))
    private lateinit var sut: ToggleSettingsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ToggleSettingsUseCase(
            testDispatcher,
            settingsRepository
        )
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
            assertEquals(false, settingsRepository.getMusicEnabled().first())
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
            assertEquals(true, settingsRepository.getSoundEnabled().first())
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
            assertEquals(false, settingsRepository.getVibrationEnabled().first())
            awaitComplete()
        }
    }
}
