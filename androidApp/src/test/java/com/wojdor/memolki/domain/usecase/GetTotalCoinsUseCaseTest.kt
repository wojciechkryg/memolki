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
class GetTotalCoinsUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetTotalCoinsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when no coins earned then return 0`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }

    @Test
    fun `when coins are added then return total coins`() = runTest {
        // given
        userRepository.addCoins(50)

        // when
        val result = sut().first()

        // then
        val expected = Result.success(50L)
        assertEquals(expected, result)
    }
}
