package com.wojdor.memolki.ui.feature.changelanguage

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import com.wojdor.memolki.test.verifyOnce
import com.wojdor.memolki.util.analytics.Analytics
import com.wojdor.memolki.util.media.HapticFeedback
import com.wojdor.memolki.util.provider.LocaleProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ChangeLanguageViewModelTest : AppTest() {

    private val analytics: Analytics by inject()

    private val hapticFeedback: HapticFeedback by inject()

    private val localeProvider: LocaleProvider by inject()

    private lateinit var sut: ChangeLanguageViewModel

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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

    @Test
    fun `when language change is ready then locale is updated and analytics is logged`() = runTest {
        // given
        sut.uiState.test {
            skipItems(1)
            awaitItem()

            val polishLanguage = sut.uiState.value.languages.first { it.tag == "pl" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(polishLanguage))
            awaitItem()

            // when
            sut.sendIntent(ChangeLanguageIntent.OnLanguageChangeReady)
            testScheduler.advanceUntilIdle()

            // then
            assertEquals("pl", localeProvider.getLanguageTag())
            verifyOnce { analytics.logLanguageChanged("en", "pl") }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when language change ready without pending language then nothing happens`() = runTest {
        // given
        testScheduler.advanceUntilIdle()
        val tagBefore = localeProvider.getLanguageTag()

        // when
        sut.sendIntent(ChangeLanguageIntent.OnLanguageChangeReady)
        testScheduler.advanceUntilIdle()

        // then
        assertEquals(tagBefore, localeProvider.getLanguageTag())
    }

    @Test
    fun `when language change fails then isLanguageChangeInProgress is reset`() = runTest {
        // given
        (localeProvider as FakeLocaleProvider).shouldThrowOnSet = true
        sut.uiState.test {
            skipItems(1)
            awaitItem()

            val polishLanguage = sut.uiState.value.languages.first { it.tag == "pl" }
            sut.sendIntent(ChangeLanguageIntent.OnLanguageClick(polishLanguage))
            awaitItem()
            assertTrue(sut.uiState.value.isLanguageChangeInProgress)

            // when
            sut.sendIntent(ChangeLanguageIntent.OnLanguageChangeReady)

            // then
            val state = awaitItem()
            assertFalse(state.isLanguageChangeInProgress)
        }
    }
}
