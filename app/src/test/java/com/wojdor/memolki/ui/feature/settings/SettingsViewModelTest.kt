package com.wojdor.memolki.ui.feature.settings

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.model.SettingModel
import com.wojdor.memolki.domain.usecase.GetSettingsUseCase
import com.wojdor.memolki.domain.usecase.ToggleSettingsUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var backgroundMusicPlayer: BackgroundMusicPlayer

    @Inject
    lateinit var getSettingsUseCase: GetSettingsUseCase

    @Inject
    lateinit var toggleSettingsUseCase: ToggleSettingsUseCase
    private lateinit var viewModel: SettingsViewModel

    override fun setup() {
        super.setup()
        viewModel = SettingsViewModel(
            savedStateHandle,
            hapticFeedback,
            backgroundMusicPlayer,
            getSettingsUseCase,
            toggleSettingsUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
