package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.shared.resources.orange
import com.wojdor.memolki.shared.resources.grape
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.empty

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetUnlockedCardPairsUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private lateinit var sut: GetUnlockedCardPairsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetUnlockedCardPairsUseCase(
            testDispatcher,
            cardRepository
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
                        CardModel.Image("banana_whole", "banana", Res.string.banana, 1),
                        CardModel.Image("banana_half", "banana", Res.string.banana, 1)
                    ),
                    CardPairModel(
                        CardModel.Image("apple_whole", "apple", Res.string.apple, 2),
                        CardModel.Text("apple_half", "apple", Res.string.apple)
                    ),
                    CardPairModel(
                        CardModel.Text("strawberry_whole", "strawberry", Res.string.strawberry),
                        CardModel.Text("strawberry_half", "strawberry", Res.string.strawberry)
                    ),
                    CardPairModel(
                        CardModel.Text("orange_whole", "orange", Res.string.orange),
                        CardModel.Text("orange_half", "orange", Res.string.orange)
                    ),
                    CardPairModel(
                        CardModel.Text("grape_whole", "grape", Res.string.grape),
                        CardModel.Text("grape_half", "grape", Res.string.grape)
                    )
                )
            )
            assertEquals(expected, result)
        }
}
