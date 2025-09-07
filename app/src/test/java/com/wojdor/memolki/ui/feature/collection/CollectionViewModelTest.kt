package com.wojdor.memolki.ui.feature.collection

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CollectionViewModelTest : AppTest() {

    private val userRepository = UserRepository(
        MockEncryptor(),
        UserLocalDataSource(MockDataStore())
    )
    private lateinit var sut: CollectionViewModel

    @Before
    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        val cardRepository = CardRepository(
            MockAllCardPairsDataSource,
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        )
        sut = CollectionViewModel(
            savedStateHandle = savedStateHandle,
            getCoinsUseCase = GetCoinsUseCase(testDispatcher, userRepository),
            getUnlockedCardPairs = GetUnlockedCardPairsUseCase(
                testDispatcher,
                cardRepository
            ),
            getAllCardPairsCountUseCase = GetAllCardPairsCountUseCase(
                testDispatcher,
                cardRepository
            )
        )
    }

    @Test
    fun `when initial load is done then the state is updated with data`() = runTest {
        sut.uiState.test {
            // given
            userRepository.addCoins(123)
            skipItems(2)

            // when
            val state = awaitItem()

            // then
            assertEquals(123, state.coins)
            assertEquals(
                MockAllCardPairsDataSource.getAllCardPairs().take(5).toModel(),
                state.unlockedCardPairs
            )
            assertEquals(5, state.lockedCardPairsCount)
        }
    }
}
