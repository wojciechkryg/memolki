package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.LevelModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetLevelsUseCaseTest : AppTest() {

    private lateinit var sut: GetLevelsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetLevelsUseCase(testDispatcher)
    }

    @Test
    fun `when called then returns list of levels`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    LevelModel.Grid2x3,
                    LevelModel.Grid3x4,
                    LevelModel.Grid4x4,
                    LevelModel.Grid4x5,
                    LevelModel.Grid5x6,
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
