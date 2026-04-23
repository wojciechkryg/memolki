package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetTotalGamesPlayedUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetTotalGamesPlayedUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when no games played then return 0`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }

    @Test
    fun `when several games played then return this amount`() = runTest {
        // given
        repeat(3) {
            userRepository.incrementTotalGamesPlayed()
        }

        // when
        val result = sut().first()

        // then
        val expected = Result.success(3L)
        assertEquals(expected, result)
    }
}
