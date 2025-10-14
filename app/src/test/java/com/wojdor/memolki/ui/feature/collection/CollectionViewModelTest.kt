package com.wojdor.memolki.ui.feature.collection

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.CollectionCardPairModel
import com.wojdor.memolki.domain.usecase.CalculateNextCardPairCostUseCase
import com.wojdor.memolki.domain.usecase.GetAllCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.GetCoinsUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsUseCase
import com.wojdor.memolki.domain.usecase.IncrementUnlockedCardPairsFromAdsCountUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardIfEnoughCoinsUseCase
import com.wojdor.memolki.domain.usecase.UnlockRandomCardUseCase
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import com.wojdor.memolki.test.fake.FakeAllCardPairsDataSource
import com.wojdor.memolki.ui.ads.AllRewardedAds
import com.wojdor.memolki.util.media.CoinsPlayer
import com.wojdor.memolki.util.media.HapticFeedback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CollectionViewModelTest : AppTest() {

    @Inject
    lateinit var savedStateHandle: SavedStateHandle

    @Inject
    lateinit var coinsPlayer: CoinsPlayer

    @Inject
    lateinit var hapticFeedback: HapticFeedback

    @Inject
    lateinit var allRewardedAds: AllRewardedAds

    @Inject
    lateinit var getCoinsUseCase: GetCoinsUseCase

    @Inject
    lateinit var getUnlockedCardPairsUseCase: GetUnlockedCardPairsUseCase

    @Inject
    lateinit var getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase

    @Inject
    lateinit var calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase

    @Inject
    lateinit var unlockRandomCardIfEnoughCoinsUseCase: UnlockRandomCardIfEnoughCoinsUseCase

    @Inject
    lateinit var unlockRandomCardUseCase: UnlockRandomCardUseCase

    @Inject
    lateinit var getUnlockedCardPairsFromAdsCountUseCase: GetUnlockedCardPairsFromAdsCountUseCase

    @Inject
    lateinit var incrementUnlockedCardPairsFromAdsCountUseCase: IncrementUnlockedCardPairsFromAdsCountUseCase

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var sut: CollectionViewModel

    @Before
    override fun setup() {
        super.setup()
        sut = CollectionViewModel(
            savedStateHandle,
            coinsPlayer,
            hapticFeedback,
            allRewardedAds,
            getCoinsUseCase,
            getUnlockedCardPairsUseCase,
            getAllCardPairsCountUseCase,
            calculateNextCardPairCostUseCase,
            unlockRandomCardIfEnoughCoinsUseCase,
            unlockRandomCardUseCase,
            getUnlockedCardPairsFromAdsCountUseCase,
            incrementUnlockedCardPairsFromAdsCountUseCase
        )
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
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
                FakeAllCardPairsDataSource().getAllCardPairs()
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
                    FakeAllCardPairsDataSource().getAllCardPairs().first().toModel()
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
