package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.log2
import kotlin.math.roundToLong

class RewardCoinsForBoardUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseParameterUseCase<BoardModel, Long>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(board: BoardModel): Flow<Result<Long>> = flow {
        val rewardedCoins = calculateRewardedCoins(board)
        userRepository.addCoins(rewardedCoins)
        emit(Result.success(rewardedCoins))
    }

    private fun calculateRewardedCoins(board: BoardModel): Long {
        val totalCards = board.columns * board.rows
        val numberOfPairs = totalCards / 2
        return (numberOfPairs * log2(numberOfPairs.toDouble()) / DIVIDE_FACTOR)
            .roundToLong()
            .coerceAtLeast(MINIMUM_REWARDED_COINS)
    }

    companion object {
        private const val DIVIDE_FACTOR = 4.5
        private const val MINIMUM_REWARDED_COINS = 1L
    }
}
