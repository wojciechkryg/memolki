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
class GetLevelUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: GetLevelUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetLevelUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when no level played count then returns default of one`() = runTest {
        // when
        sut("2x3").test {
            // then
            assertEquals(Result.success(1L), awaitItem())
        }
    }

    @Test
    fun `when level was played then returns incremented count`() = runTest {
        // given
        userRepository.incrementLevel("2x3")

        // when
        sut("2x3").test {
            // then
            assertEquals(Result.success(2L), awaitItem())
        }
    }

    @Test
    fun `when different levels then returns independent counts`() = runTest {
        // given
        userRepository.incrementLevel("2x3")
        userRepository.incrementLevel("2x3")
        userRepository.incrementLevel("4x4")

        // when / then
        sut("2x3").test {
            assertEquals(Result.success(3L), awaitItem())
        }
        sut("4x4").test {
            assertEquals(Result.success(2L), awaitItem())
        }
    }
}
