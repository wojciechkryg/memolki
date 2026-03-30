package com.wojdor.memolki.ui.feature.changelanguage

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.domain.usecase.GetLanguagesWithCurrentUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.verifyOnce
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.LocaleProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChangeLanguageViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var getLanguagesWithCurrentUseCase: GetLanguagesWithCurrentUseCase

    @Inject
    lateinit var localeProvider: LocaleProvider

    private lateinit var sut: ChangeLanguageViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = ChangeLanguageViewModel(
            savedStateHandle,
            hapticFeedback,
            getLanguagesWithCurrentUseCase,
            localeProvider
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when created then loads languages`() = runTest {
        // when
        sut.uiState.test {
            skipItems(1)

            // then
            val state = awaitItem()
            assertEquals(32, state.languages.size)
            assertEquals("en", state.currentLanguage.tag)
        }
    }

    @Test
    fun `when language click then sets language change in progress`() = runTest {
        // given
        sut.uiState.test {
            skipItems(1)
            awaitItem()

            // when
            val polishLanguage = sut.uiState.value.languages.first { it.tag == "pl" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(polishLanguage))

            // then
            val state = awaitItem()
            assertTrue(state.isLanguageChangeInProgress)
        }
    }

    @Test
    fun `when same language click then does not change state`() = runTest {
        // given
        sut.uiState.test {
            skipItems(1)
            awaitItem()

            // when
            val englishLanguage = sut.uiState.value.languages.first { it.tag == "en" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(englishLanguage))

            // then
            assertFalse(sut.uiState.value.isLanguageChangeInProgress)
            expectNoEvents()
        }
    }

    @Test
    fun `when language click then vibrates`() = runTest {
        // given
        sut.uiState.test {
            skipItems(1)
            awaitItem()

            // when
            val polishLanguage = sut.uiState.value.languages.first { it.tag == "pl" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(polishLanguage))
            awaitItem()
        }

        // then
        verifyOnce { hapticFeedback.vibrateLow() }
    }

    @Test
    fun `when already changing language then ignores click`() = runTest {
        // given
        sut.uiState.test {
            skipItems(1)
            awaitItem()
            val polishLanguage = sut.uiState.value.languages.first { it.tag == "pl" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(polishLanguage))
            awaitItem()
            assertTrue(sut.uiState.value.isLanguageChangeInProgress)

            // when
            val germanLanguage = sut.uiState.value.languages.first { it.tag == "de" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(germanLanguage))

            // then
            expectNoEvents()
        }
    }
}
