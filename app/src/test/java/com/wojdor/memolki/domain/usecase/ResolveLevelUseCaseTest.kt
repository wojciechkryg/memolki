package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ResolveLevelUseCaseTest : AppTest() {

    @Inject
    lateinit var getLevelsUseCase: GetLevelsUseCase

    @Inject
    lateinit var unlockedCardPairsLocalDataSource: UnlockedCardPairsLocalDataSource

    private lateinit var sut: ResolveLevelUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = ResolveLevelUseCase(testDispatcher, getLevelsUseCase)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when requested level is unlocked then returns requested level`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")

        // when
        sut("3x4").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("3x4", result.getOrThrow().id)
            assertTrue(result.getOrThrow().isUnlocked)
            awaitComplete()
        }
    }

    @Test
    fun `when requested level is locked then returns biggest unlocked level`() = runTest {
        // when
        sut("5x6").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("2x3", result.getOrThrow().id)
            awaitComplete()
        }
    }

    @Test
    fun `when unknown level requested then returns biggest unlocked level`() = runTest {
        // given
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("watermelon")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("mango")
        unlockedCardPairsLocalDataSource.addUnlockedCardPairId("peach")

        // when
        sut("auto").test {
            // then
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals("4x4", result.getOrThrow().id)
            awaitComplete()
        }
    }
}
