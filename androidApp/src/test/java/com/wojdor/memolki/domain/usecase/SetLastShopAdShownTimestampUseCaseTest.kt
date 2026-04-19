package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SetLastShopAdShownTimestampUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: SetLastShopAdShownTimestampUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = SetLastShopAdShownTimestampUseCase(
            testDispatcher,
            userRepository
        )
    }

    @Test
    fun `when invoked then timestamp is stored`() = runTest {
        // when
        val result = sut().first()

        // then
        assertEquals(Result.success(Unit), result)
        assertTrue(userRepository.getLastShopAdShownTimestamp().first() > 0)
    }
}
