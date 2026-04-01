package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CanUnlockNewCardUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Boolean>> =
        combine(
            getCoinsUseCase(),
            calculateNextCardPairCostUseCase()
        ) { coinsResult, nextCardCostResult ->
            val coins = coinsResult.getOrThrow()
            val nextCardCost = nextCardCostResult.getOrThrow()
            if (nextCardCost == CalculateNextCardPairCostUseCase.NO_MORE_CARDS) {
                Result.success(false)
            } else {
                Result.success(coins >= nextCardCost)
            }
        }
}
