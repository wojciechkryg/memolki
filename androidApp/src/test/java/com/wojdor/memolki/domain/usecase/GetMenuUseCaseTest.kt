package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.domain.model.MenuModel
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get

@ExperimentalCoroutinesApi
class GetMenuUseCaseTest : AppTest() {

    private lateinit var sut: GetMenuUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when called then returns list of menu items`() = runTest {
        // when
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    MenuModel.Play,
                    MenuModel.Collection,
                    MenuModel.Leaderboard,
                    MenuModel.Settings
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
