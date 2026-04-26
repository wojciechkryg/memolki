package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.get
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SetLastShopAdShownTimestampUseCaseTest : AppTest() {

    private val userRepository: UserRepository by inject()

    private lateinit var sut: SetLastShopAdShownTimestampUseCase

    @BeforeTest
    override fun setup() {
        super.setup()
        sut = get()
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
