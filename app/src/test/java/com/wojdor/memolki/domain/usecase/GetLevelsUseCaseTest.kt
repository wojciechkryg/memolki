package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetLevelsUseCaseTest : AppTest() {

    @Inject
    lateinit var getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase

    private lateinit var sut: GetLevelsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetLevelsUseCase(
            testDispatcher,
            getUnlockedCardPairsCountUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
