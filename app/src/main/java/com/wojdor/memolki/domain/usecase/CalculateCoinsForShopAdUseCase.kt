package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalculateCoinsForShopAdUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getBoardsUseCase: GetBoardsUseCase
) : BaseUseCase<Long>(coroutineDispatcher) {

    override fun execute() = flow {
        val coinsToReward = calculateRewardedCoins()
        if (coinsToReward > DEFAULT_REWARDED_COINS) {
            emit(Result.success(coinsToReward))
        } else {
            emit(Result.failure(Exception("Wrongly calculated rewarded coins from ad")))
        }
    }

    private suspend fun calculateRewardedCoins(): Long {
        val unlockedBoards = getBoardsUseCase().first().getOrNull() ?: return DEFAULT_REWARDED_COINS
        val biggestUnlockedBoard = unlockedBoards.filter { it.isUnlocked }.maxByOrNull { it.id }
        return biggestUnlockedBoard?.let {
            it.columns * it.rows.toLong()
        } ?: DEFAULT_REWARDED_COINS
    }

    companion object {
        private const val DEFAULT_REWARDED_COINS = 0L
    }
}
