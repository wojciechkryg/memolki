package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetUnlockedCardPairsCountUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private lateinit var sut: GetUnlockedCardPairsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetUnlockedCardPairsCountUseCase(
            testDispatcher,
            cardRepository
        )
    }

    @Test
    fun `when called with default unlocked card pairs then return success result with default unlocked cards count`() =
        runTest {
            // when
            val result = sut().first()

            // then
            val expected = Result.success(5)
            assertEquals(expected, result)
        }
}
