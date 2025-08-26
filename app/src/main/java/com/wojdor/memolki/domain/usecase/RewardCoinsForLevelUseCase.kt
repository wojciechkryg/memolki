package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.math.log2
import kotlin.math.roundToLong

class RewardCoinsForLevelUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseParameterUseCase<LevelModel, Long>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(level: LevelModel): Flow<Result<Long>> = flow {
        val rewardedCoins = calculateRewardedCoins(level)
        userRepository.addCoins(rewardedCoins)
        emit(Result.success(rewardedCoins))
    }

    private fun calculateRewardedCoins(level: LevelModel): Long {
        val totalCards = level.columns * level.rows
        val numberOfPairs = totalCards / 2
        return (numberOfPairs * log2(numberOfPairs.toDouble()) / DIVIDE_FACTOR).roundToLong()
    }

    companion object {
        private const val DIVIDE_FACTOR = 4.5
    }
}
