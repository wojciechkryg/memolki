package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUnlockedCardPairsUseCaseTest : AppTest() {

    private lateinit var sut: GetUnlockedCardPairsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetUnlockedCardPairsUseCase(
            testDispatcher,
            CardRepository(
                MockAllCardPairsDataSource, UnlockedCardPairsLocalDataSource(
                    MockDataStore(), MockAllCardPairsDataSource
                )
            )
        )
    }

    @Test
    fun `when called with default unlocked card pairs then return success result with default unlocked cards`() =
        runTest {
            // when
            val result = sut().first()

            // then
            val expected = Result.success(
                listOf(
                    CardPairModel(
                        CardModel.Image("banana_whole", "banana", 1, 1),
                        CardModel.Image("banana_half", "banana", 1, 1)
                    ),
                    CardPairModel(
                        CardModel.Image("apple_whole", "apple", 2, 2),
                        CardModel.Text("apple_half", "apple", 2)
                    ),
                    CardPairModel(
                        CardModel.Text("strawberry_whole", "strawberry", 3),
                        CardModel.Text("strawberry_half", "strawberry", 3)
                    ),
                    CardPairModel(
                        CardModel.Text("orange_whole", "orange", 4),
                        CardModel.Text("orange_half", "orange", 4)
                    ),
                    CardPairModel(
                        CardModel.Text("grape_whole", "grape", 5),
                        CardModel.Text("grape_half", "grape", 5)
                    )
                )
            )
            assertEquals(expected, result)
        }
}
