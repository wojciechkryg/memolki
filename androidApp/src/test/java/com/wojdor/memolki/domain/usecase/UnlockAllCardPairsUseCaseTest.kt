package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.test.AppTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class UnlockAllCardPairsUseCaseTest : AppTest() {

    private val cardRepository: CardRepository by inject()

    private lateinit var sut: UnlockAllCardPairsUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when use case is executed then unlock all card pairs in repository`() = runTest {
        // when
        sut().first()

        // then
        assertTrue(cardRepository.getLockedCardPairs().isEmpty())
    }
}
