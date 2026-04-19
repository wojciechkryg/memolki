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
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
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

    @Before
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

    @Test
    fun `when sound setting is toggled then it is updated to disabled`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val soundSetting = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Sound>().first()
        assertTrue(soundSetting.isEnabled)

        // when
        viewModel.sendIntent(SettingsIntent.OnSettingClick(soundSetting))
        testScheduler.advanceUntilIdle()

        // then
        val updatedSound = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Sound>().first()
        assertFalse(updatedSound.isEnabled)
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when music setting is toggled then it is updated to disabled`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val musicSetting = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Music>().first()
        assertTrue(musicSetting.isEnabled)

        // when
        viewModel.sendIntent(SettingsIntent.OnSettingClick(musicSetting))
        testScheduler.advanceUntilIdle()

        // then
        val updatedMusic = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Music>().first()
        assertFalse(updatedMusic.isEnabled)
    }

    @Test
    fun `when music setting is toggled to disabled then background music is paused`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val musicSetting = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Music>().first()

        // when
        viewModel.sendIntent(SettingsIntent.OnSettingClick(musicSetting))
        testScheduler.advanceUntilIdle()

        // then
        verify { backgroundMusicPlayer.pause() }
    }

    @Test
    fun `when music setting is toggled to enabled then background music is started`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val musicSetting = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Music>().first()
        // Disable first
        viewModel.sendIntent(SettingsIntent.OnSettingClick(musicSetting))
        testScheduler.advanceUntilIdle()

        // when - re-enable
        val disabledMusic = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Music>().first()
        viewModel.sendIntent(SettingsIntent.OnSettingClick(disabledMusic))
        testScheduler.advanceUntilIdle()

        // then
        verify { backgroundMusicPlayer.start() }
    }

    @Test
    fun `when vibration setting is toggled then it is updated to disabled`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val vibrationSetting = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Vibration>().first()
        assertTrue(vibrationSetting.isEnabled)

        // when
        viewModel.sendIntent(SettingsIntent.OnSettingClick(vibrationSetting))
        testScheduler.advanceUntilIdle()

        // then
        val updatedVibration = viewModel.uiState.value.settings
            .filterIsInstance<SettingModel.Vibration>().first()
        assertFalse(updatedVibration.isEnabled)
        verify { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when language click then OpenChangeLanguageScreen effect is sent`() = runTest {
        viewModel.uiEffect.test {
            // when
            viewModel.sendIntent(SettingsIntent.OnLanguageClick)

            // then
            assertEquals(SettingsEffect.OpenChangeLanguageScreen, awaitItem())
        }
    }
}
