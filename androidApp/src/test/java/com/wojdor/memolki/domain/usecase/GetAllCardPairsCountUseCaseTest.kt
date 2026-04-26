package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetAllCardPairsCountUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private lateinit var sut: GetAllCardPairsCountUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when use case is called then it returns all cards count`() =
        runTest {
            // when
            sut().test {
                // then
                assertEquals(Result.success(10), awaitItem())
                awaitComplete()
            }
        }
}
