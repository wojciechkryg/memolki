package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ChangeLanguageUseCaseTest : AppTest() {

    private val fakeLocaleProvider: FakeLocaleProvider by inject()

    private lateinit var sut: ChangeLanguageUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ChangeLanguageUseCase(testDispatcher, fakeLocaleProvider)
    }

    @Test
    fun `when called then sets language tag`() = runTest {
        // when
        sut("pl").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("pl", fakeLocaleProvider.getLanguageTag())
            awaitComplete()
        }
    }
}
