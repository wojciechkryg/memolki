package com.wojdor.memolki.ui.feature.collection

import app.cash.turbine.test
import com.wojdor.memolki.data.local.card.UnlockedCardPairsLocalDataSource
import com.wojdor.memolki.data.local.user.UserLocalDataSource
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetLevelsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.mock.MockAllCardPairsDataSource
import com.wojdor.memolki.test.mock.MockDataStore
import com.wojdor.memolki.test.mock.MockEncryptor
import com.wojdor.memolki.test.relaxedMockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CollectionViewModelTest : AppTest() {

    private lateinit var userRepository: UserRepository
    private lateinit var sut: CollectionViewModel

    @Before
    override fun setup() {
        super.setup()
        val dataStore = MockDataStore()
        val cardRepository = CardRepository(
            MockAllCardPairsDataSource,
            UnlockedCardPairsLocalDataSource(dataStore, MockAllCardPairsDataSource)
        )
        userRepository = UserRepository(
            MockEncryptor(),
            UserLocalDataSource(MockDataStore())
        )
        val calculateNextCardPairCostUseCase = CalculateNextCardPairCostUseCase(
            testDispatcher,
            GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository),
            GetLevelsUseCase(
                testDispatcher,
                GetUnlockedCardPairsCountUseCase(testDispatcher, cardRepository)
            ),
            cardRepository
        )
        sut = CollectionViewModel(
            savedStateHandle = savedStateHandle,
            hapticFeedback = relaxedMockk(),
            getCoinsUseCase = GetCoinsUseCase(testDispatcher, userRepository),
            getUnlockedCardPairs = GetUnlockedCardPairsUseCase(
                testDispatcher,
                cardRepository
            ),
            getAllCardPairsCountUseCase = GetAllCardPairsCountUseCase(
                testDispatcher,
                cardRepository
            ),
            calculateNextCardPairCostUseCase = calculateNextCardPairCostUseCase,
            unlockRandomCardIfEnoughCoinsUseCase = UnlockRandomCardIfEnoughCoinsUseCase(
                testDispatcher,
                calculateNextCardPairCostUseCase,
                cardRepository,
                userRepository
            ),
            coinsPlayer = relaxedMockk(),
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
                MockAllCardPairsDataSource.getAllCardPairs()
                    .take(5)
                    .toModel(),
                state.collectionCardPairs
                    .take(5)
                    .filter { it is CollectionCardPairModel.Unlocked }
                    .map { (it as CollectionCardPairModel.Unlocked).cardPair },
            )
            assertEquals(5, state.unlockedCardPairsCount)
        }
    }

    @Test
    fun `when OnCardPairClick intent is send then the OpenCardPairDetailsScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // given
                val unlockedCardPair = CollectionCardPairModel.Unlocked(
                    MockAllCardPairsDataSource.getAllCardPairs().first().toModel()
                )

                // when
                sut.sendIntent(CollectionIntent.OnCardPairClick(unlockedCardPair))

                // then
                assertEquals(
                    CollectionEffect.OpenCardPairDetailsScreen(unlockedCardPair.cardPair),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when OnShopClick intent is send then the OpenShopScreen effect is send`() =
        runTest {
            sut.uiEffect.test {
                // when
                sut.sendIntent(CollectionIntent.OnShopClick)

                // then
                assertEquals(CollectionEffect.OpenShopScreen, awaitItem())
            }
        }
}
