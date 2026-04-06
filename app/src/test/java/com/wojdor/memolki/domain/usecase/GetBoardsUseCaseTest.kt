package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetBoardsUseCaseTest : AppTest() {

    @Inject
    lateinit var getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase

    private lateinit var sut: GetBoardsUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetBoardsUseCase(
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
                    BoardModel.Grid2x3(isUnlocked = true),
                    BoardModel.Grid3x4(isUnlocked = false),
                    BoardModel.Grid4x4(isUnlocked = false),
                    BoardModel.Grid4x5(isUnlocked = false),
                    BoardModel.Grid4x6(isUnlocked = false),
                    BoardModel.Grid5x6(isUnlocked = false),
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
