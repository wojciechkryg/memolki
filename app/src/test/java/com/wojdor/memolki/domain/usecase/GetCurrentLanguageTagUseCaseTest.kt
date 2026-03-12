package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeLocaleProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetCurrentLanguageTagUseCaseTest : AppTest() {

    @Inject
    lateinit var fakeLocaleProvider: FakeLocaleProvider

    private lateinit var sut: GetCurrentLanguageTagUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetCurrentLanguageTagUseCase(testDispatcher, fakeLocaleProvider)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
