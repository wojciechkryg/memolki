package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CalculateCoinsForShopAdUseCaseTest : AppTest() {

    @Inject
    lateinit var getLevelsUseCase: GetLevelsUseCase

    private lateinit var sut: CalculateCoinsForShopAdUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = CalculateCoinsForShopAdUseCase(testDispatcher, getLevelsUseCase)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when there is one unlocked level then calculate coins based on it`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(6L), result)
    }
}
