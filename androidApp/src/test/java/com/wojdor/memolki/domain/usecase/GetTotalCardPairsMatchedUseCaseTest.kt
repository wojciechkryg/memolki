package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetTotalCardPairsMatchedUseCaseTest : AppTest() {

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: GetTotalCardPairsMatchedUseCase

    @Before
    override fun setup() {
        super.setup()
        sut = GetTotalCardPairsMatchedUseCase(
            testDispatcher,
            userRepository
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when no card pairs matched then return 0`() = runTest {
        // when
        val result = sut().first()

        // then
        val expected = Result.success(0L)
        assertEquals(expected, result)
    }

    @Test
    fun `when several card pairs matched then return total`() = runTest {
        // given
        repeat(3) {
            userRepository.incrementTotalCardPairsMatched()
        }

        // when
        val result = sut().first()

        // then
        val expected = Result.success(3L)
        assertEquals(expected, result)
    }
}
