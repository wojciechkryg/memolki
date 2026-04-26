package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.watermelon
import com.wojdor.memolki.shared.resources.strawberry
import com.wojdor.memolki.shared.resources.pineapple
import com.wojdor.memolki.shared.resources.peach
import com.wojdor.memolki.shared.resources.orange
import com.wojdor.memolki.shared.resources.mango
import com.wojdor.memolki.shared.resources.grape
import com.wojdor.memolki.shared.resources.blueberry
import com.wojdor.memolki.shared.resources.banana
import com.wojdor.memolki.shared.resources.apple
import com.wojdor.memolki.shared.resources.empty

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetAllCardPairsUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private lateinit var sut: GetAllCardPairsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when use case is called then returns all card pairs`() =
        runTest {
            // when
            sut().test {
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
                        ),
                        CardPairModel(
                            CardModel.Text("watermelon_whole", "watermelon", Res.string.watermelon),
                            CardModel.Text("watermelon_half", "watermelon", Res.string.watermelon)
                        ),
                        CardPairModel(
                            CardModel.Text("mango_whole", "mango", Res.string.mango),
                            CardModel.Text("mango_half", "mango", Res.string.mango)
                        ),
                        CardPairModel(
                            CardModel.Text("peach_whole", "peach", Res.string.peach),
                            CardModel.Text("peach_half", "peach", Res.string.peach)
                        ),
                        CardPairModel(
                            CardModel.Text("pineapple_whole", "pineapple", Res.string.pineapple),
                            CardModel.Text("pineapple_half", "pineapple", Res.string.pineapple)
                        ),
                        CardPairModel(
                            CardModel.Text("blueberry_whole", "blueberry", Res.string.blueberry),
                            CardModel.Text("blueberry_half", "blueberry", Res.string.blueberry)
                        )
                    )
                )
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
