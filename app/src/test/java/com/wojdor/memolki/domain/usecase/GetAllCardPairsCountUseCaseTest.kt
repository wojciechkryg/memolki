package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllCardPairsCountUseCaseTest : AppTest() {

    private lateinit var sut: GetAllCardPairsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetAllCardPairsCountUseCase(
            testDispatcher,
            CardRepository(
                MockAllCardPairsDataSource,
                UnlockedCardPairsLocalDataSource(
                    MockDataStore(),
                    MockAllCardPairsDataSource
                )
            )
        )
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
