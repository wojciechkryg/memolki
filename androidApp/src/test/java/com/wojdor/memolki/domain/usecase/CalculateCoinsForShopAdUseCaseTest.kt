package com.wojdor.memolki.domain.usecase

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
class CalculateCoinsForShopAdUseCaseTest : AppTest() {

    private val getBoardsUseCase: GetBoardsUseCase by inject()

    private lateinit var sut: CalculateCoinsForShopAdUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when there is one unlocked level then calculate coins based on it`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(6L), result)
    }
}
