package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetLevelUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetLevelUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when no level then returns default of one`() = runTest {
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
