package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class CanUnlockNewCardUseCaseTest : AppTest() {

    private val getCoinsUseCase: GetCoinsUseCase by inject()

    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase by inject()

    private val userRepository: UserRepository by inject()

    private lateinit var sut: CanUnlockNewCardUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
    }

    @Test
    fun `when user does not have enough coins then return false`() = runTest {
        // when
        val result = sut().first()

        // then
        assertFalse(result.getOrThrow())
    }

    @Test
    fun `when user has enough coins then return true`() = runTest {
        // given
        userRepository.addCoins(11L)

        // when
        val result = sut().first()

        // then
        assertTrue(result.getOrThrow())
    }
}
