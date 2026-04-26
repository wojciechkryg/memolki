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
class GetCurrentLanguageTagUseCaseTest : AppTest() {

    private val fakeLocaleProvider: FakeLocaleProvider by inject()

    private lateinit var sut: GetCurrentLanguageTagUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when locale is set then returns language tag`() = runTest {
        // given
        fakeLocaleProvider.setLanguageTag("pl")

        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("pl", result.getOrThrow())
            awaitComplete()
        }
    }

    @Test
    fun `when locale is english then returns en`() = runTest {
        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("en", result.getOrThrow())
            awaitComplete()
        }
    }
}
