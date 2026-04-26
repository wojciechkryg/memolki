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
class GetTotalCardPairsMatchedUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetTotalCardPairsMatchedUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when no card pairs matched then return 0`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }

    @Test
    fun `when several card pairs matched then return total`() = runTest {
        // given
        repeat(3) {
            userRepository.incrementTotalCardPairsMatched()
        }

        // when
        val result = sut().first()

        // then
        val expected = Result.success(3L)
        assertEquals(expected, result)
    }
}
