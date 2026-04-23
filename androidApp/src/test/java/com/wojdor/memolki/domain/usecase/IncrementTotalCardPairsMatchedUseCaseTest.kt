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
class IncrementTotalCardPairsMatchedUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: IncrementTotalCardPairsMatchedUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when execute is successful then returns success`() = runTest {
        // when
        sut().test {
            // then
            assertEquals(Result.success(1L), awaitItem())
            awaitComplete()
        }
        userRepository.getTotalCardPairsMatched().test {
            assertEquals(1L, awaitItem())
        }
    }
}

