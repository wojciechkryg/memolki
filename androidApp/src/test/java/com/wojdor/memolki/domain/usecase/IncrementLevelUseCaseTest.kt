package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class IncrementLevelUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: IncrementLevelUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
