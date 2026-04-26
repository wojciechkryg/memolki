package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetCoinsUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetCoinsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when get coins is successful with coins then return success result`() = runTest {
        // given
        val coins = 123L
        userRepository.addCoins(coins)

        // when
        val result = sut().first()

        // then
        val expected = Result.success(coins)
        assertEquals(expected, result)
    }

    @Test
    fun `when get coins is empty then return empty success result`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }
}
