package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class IncrementUnlockedCardPairsFromAdsCountUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: IncrementUnlockedCardPairsFromAdsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when use case is executed then should increment unlocked card pairs from ads count`() =
        runTest {
            // when
            sut().first()

            // then
            assertEquals(1, userRepository.getUnlockedCardPairsFromAdsCount().first())
        }
}
