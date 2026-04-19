package com.wojdor.memolki.domain.usecase

import app.cash.turbine.test
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetAllCardPairsCountUseCaseTest : AppTest() {

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var sut: GetAllCardPairsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetAllCardPairsCountUseCase(
            testDispatcher,
            cardRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when use case is called then it returns all cards count`() =
        runTest {
            // when
            sut().test {
                // then
                assertEquals(Result.success(10), awaitItem())
                awaitComplete()
            }
        }
}
