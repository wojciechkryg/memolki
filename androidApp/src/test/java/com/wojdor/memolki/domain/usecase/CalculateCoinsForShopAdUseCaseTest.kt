package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class CalculateCoinsForShopAdUseCaseTest : AppTest() {

    private val getBoardsUseCase: GetBoardsUseCase by inject()

    private lateinit var sut: CalculateCoinsForShopAdUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CalculateCoinsForShopAdUseCase(testDispatcher, getBoardsUseCase)
    }

    @Test
    fun `when there is one unlocked level then calculate coins based on it`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(6L), result)
    }
}
