package com.wojdor.memolki.ui.feature.settings

import app.cash.turbine.test
import com.wojdor.memolki.data.local.settings.SettingsLocalDataSource
import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import com.wojdor.memolki.domain.usecase.ToggleSettingsUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest : AppTest() {

    private lateinit var settingsLocalDataSource: SettingsLocalDataSource
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var getSettingsUseCase: GetSettingsUseCase
    private lateinit var toggleSettingsUseCase: ToggleSettingsUseCase
    private val hapticFeedback: HapticFeedback = mockk(relaxed = true)
    private val backgroundMusicPlayer: BackgroundMusicPlayer = mockk(relaxed = true)
    private lateinit var viewModel: SettingsViewModel

    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        settingsLocalDataSource = SettingsLocalDataSource(dataStore)
        settingsRepository = SettingsRepository(settingsLocalDataSource)
        getSettingsUseCase = GetSettingsUseCase(testDispatcher, settingsRepository)
        toggleSettingsUseCase = ToggleSettingsUseCase(testDispatcher, settingsRepository)
        viewModel = SettingsViewModel(
            savedStateHandle,
            hapticFeedback,
            backgroundMusicPlayer,
            getSettingsUseCase,
            toggleSettingsUseCase
        )
    }

    @Test
    fun `When view model is created then should load settings`() = runTest {
        // when
        viewModel.uiState.test {
            skipItems(1)

            // then
            val expectedSettings = listOf(
                SettingModel.Music(isEnabled = true),
                SettingModel.Sound(isEnabled = true),
                SettingModel.Vibration(isEnabled = true)
            )
            assertEquals(expectedSettings, awaitItem().settings)
        }
    }
}
