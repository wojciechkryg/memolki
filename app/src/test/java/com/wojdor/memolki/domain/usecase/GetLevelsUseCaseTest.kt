package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetLevelsUseCaseTest : AppTest() {

    private val cardRepository = CardRepository(
        MockAllCardPairsDataSource, UnlockedCardPairsLocalDataSource(
            MockDataStore(), MockAllCardPairsDataSource
        )
    )
    private lateinit var sut: GetLevelsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetLevelsUseCase(
            testDispatcher,
            GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository)
        )
    }

    @Test
    fun `when called then returns list of levels`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    LevelModel.Grid2x3(isUnlocked = true),
                    LevelModel.Grid3x4(isUnlocked = false),
                    LevelModel.Grid4x4(isUnlocked = false),
                    LevelModel.Grid4x5(isUnlocked = false),
                    LevelModel.Grid5x6(isUnlocked = false),
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
