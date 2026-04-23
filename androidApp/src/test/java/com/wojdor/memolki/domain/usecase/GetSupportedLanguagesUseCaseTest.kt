package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.get

@ExperimentalCoroutinesApi
class GetSupportedLanguagesUseCaseTest : AppTest() {

    private lateinit var sut: GetSupportedLanguagesUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when called then returns all supported languages`() = runTest {
        // when
        sut().test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(32, result.getOrThrow().size)
            awaitComplete()
        }
    }

    @Test
    fun `when called then english is included`() = runTest {
        // when
        sut().test {
            // then
            val languages = awaitItem().getOrThrow()
            assertTrue(languages.any { it.tag == "en" })
            awaitComplete()
        }
    }

    @Test
    fun `when called then all tags are unique`() = runTest {
        // when
        sut().test {
            // then
            val languages = awaitItem().getOrThrow()
            val tags = languages.map { it.tag }
            assertEquals(tags.size, tags.distinct().size)
            awaitComplete()
        }
    }
}
