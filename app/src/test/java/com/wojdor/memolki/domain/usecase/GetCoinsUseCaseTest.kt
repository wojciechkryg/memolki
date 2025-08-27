package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCoinsUseCaseTest : AppTest() {

    private val userRepository = UserRepository(
        MockEncryptor(),
        UserLocalDataSource(MockDataStore())
    )
    private lateinit var sut: GetCoinsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetCoinsUseCase(
            testDispatcher,
            userRepository
        )
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
