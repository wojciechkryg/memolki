package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class GetUnlockedCardPairsFromAdsCountUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: GetUnlockedCardPairsFromAdsCountUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetUnlockedCardPairsFromAdsCountUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when use case is executed then should return unlocked card pairs from ads count`() =
        runTest {
            // given
            userRepository.incrementUnlockedCardPairsFromAdsCount()
            userRepository.incrementUnlockedCardPairsFromAdsCount()

            // when
            val result = sut().first()

            // then
            assertEquals(Result.success(2L), result)
        }
}
