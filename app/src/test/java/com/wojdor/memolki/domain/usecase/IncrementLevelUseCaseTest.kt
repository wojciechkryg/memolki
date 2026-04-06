package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class IncrementLevelUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: IncrementLevelUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = IncrementLevelUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when execute is successful then returns incremented count`() = runTest {
        // when
        sut("2x3").test {
            // then
            assertEquals(Result.success(2L), awaitItem())
            awaitComplete()
        }
        userRepository.getLevel("2x3").test {
            assertEquals(2L, awaitItem())
        }
    }

    @Test
    fun `when executed twice then returns second incremented count`() = runTest {
        // given
        sut("2x3").test {
            awaitItem()
            awaitComplete()
        }

        // when
        sut("2x3").test {
            // then
            assertEquals(Result.success(3L), awaitItem())
            awaitComplete()
        }
    }
}
