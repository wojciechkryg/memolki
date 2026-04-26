package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetLanguagesWithCurrentUseCaseTest : AppTest() {

    private val getSupportedLanguagesUseCase: GetSupportedLanguagesUseCase by inject()

    private val fakeLocaleProvider: FakeLocaleProvider by inject()

    private lateinit var sut: GetLanguagesWithCurrentUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        val getCurrentLanguageTagUseCase = GetCurrentLanguageTagUseCase(
            testDispatcher,
            fakeLocaleProvider
        )
        sut = get()
    }

    @Test
    fun `when locale is english then returns english as current language`() = runTest {
        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            val (languages, currentLanguage) = result.getOrThrow()
            assertEquals(32, languages.size)
            assertEquals("en", currentLanguage.tag)
            awaitComplete()
        }
    }

    @Test
    fun `when locale is polish then returns polish as current language`() = runTest {
        // given
        fakeLocaleProvider.setLanguageTag("pl")

        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            val (languages, currentLanguage) = result.getOrThrow()
            assertEquals(32, languages.size)
            assertEquals("pl", currentLanguage.tag)
            awaitComplete()
        }
    }

    @Test
    fun `when locale is unknown then falls back to english`() = runTest {
        // given
        fakeLocaleProvider.setLanguageTag("xx")

        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            val (_, currentLanguage) = result.getOrThrow()
            assertEquals("en", currentLanguage.tag)
            awaitComplete()
        }
    }
}
