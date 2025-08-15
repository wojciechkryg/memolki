package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.AppTest
import com.wojdor.memolki.domain.model.MenuModel
import io.mockk.impl.annotations.InjectMockKs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMenuUseCaseTest : AppTest() {

    private lateinit var sut: GetMenuUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetMenuUseCase(testDispatcher)
    }

    @Test
    fun `when called then returns list of menu items`() = runTest {
        sut().test {
            // then
            val expected = Result.success(
                listOf(
                    MenuModel.NewGame,
                    MenuModel.Collection,
                    MenuModel.Settings
                )
            )
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}
