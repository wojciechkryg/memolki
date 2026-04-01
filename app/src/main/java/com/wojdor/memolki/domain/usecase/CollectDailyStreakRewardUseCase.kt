package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CollectDailyStreakRewardUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider,
    private val checkDailyLoginStreakUseCase: CheckDailyLoginStreakUseCase
) : BaseUseCase<Long>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Long>> = flow {
        val streakResult = checkDailyLoginStreakUseCase().first().getOrThrow()
        userRepository.addCoins(streakResult.coinsReward)
        userRepository.setDailyStreakData(
            count = streakResult.streakDay.toLong(),
            timestamp = timeProvider.currentTimeMillis()
        )
        emit(Result.success(streakResult.coinsReward))
    }
}
