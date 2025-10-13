package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import kotlin.random.Random

@ExperimentalCoroutinesApi
class GetShuffledUnlockedCardsTest : AppTest() {

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var sut: GetShuffledUnlockedCardsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetShuffledUnlockedCardsUseCase(
            testDispatcher,
            cardRepository,
            Random(0)
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when called then returns shuffled unlocked card pair ids`() = runTest {
        // given
        val level = LevelModel.Grid2x3()

        // when
        sut(level).test {
            // then
            val result = awaitItem().getOrElse { listOf() }
            val notExpected = Result.success(
                listOf(
                    "banana",
                    "apple",
                    "strawberry",
                    "orange",
                    "grape",
                    "watermelon"
                )
            )
            assertEquals(6, result.size)
            assertNotEquals(notExpected.getOrNull(), result)
            awaitComplete()
        }
    }
}
