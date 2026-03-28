package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CheckDailyLoginStreakUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider
) : BaseUseCase<CheckDailyLoginStreakUseCase.DailyStreakResult>(coroutineDispatcher) {

    override fun execute(): Flow<Result<DailyStreakResult>> = flow {
        val result = runCatching { checkStreak() }
        emit(result)
    }

    private suspend fun checkStreak(): DailyStreakResult {
        val lastTimestamp = userRepository.getLastDailyStreakCollectedTimestamp().first()
        val currentStreak = userRepository.getDailyStreakCount().first()
        val now = timeProvider.currentTimeMillis()
        val today = timeProvider.currentLocalDate()
        if (lastTimestamp == 0L) {
            return DailyStreakResult(
                isRewardAvailable = true,
                streakDay = 1,
                coinsReward = calculateReward(1)
            )
        }
        if (now < lastTimestamp) {
            return DailyStreakResult(
                isRewardAvailable = false,
                streakDay = currentStreak.toInt(),
                coinsReward = 0L
            )
        }
        val lastCollectedDate = Instant.ofEpochMilli(lastTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val daysBetween = ChronoUnit.DAYS.between(lastCollectedDate, today)
        return when {
            daysBetween == 0L -> DailyStreakResult(
                isRewardAvailable = false,
                streakDay = currentStreak.toInt(),
                coinsReward = 0L
            )
            daysBetween == 1L -> {
                val newStreak = (currentStreak + 1).toInt()
                DailyStreakResult(
                    isRewardAvailable = true,
                    streakDay = newStreak,
                    coinsReward = calculateReward(newStreak)
                )
            }
            else -> DailyStreakResult(
                isRewardAvailable = true,
                streakDay = 1,
                coinsReward = calculateReward(1)
            )
        }
    }

    data class DailyStreakResult(
        val isRewardAvailable: Boolean,
        val streakDay: Int,
        val coinsReward: Long
    )

    companion object {
        private fun calculateReward(streakDay: Int): Long = when {
            streakDay >= MAX_REWARD_DAY -> MAX_DAILY_REWARD
            else -> streakDay.toLong()
        }
        const val MAX_DAILY_REWARD = 5L
        private const val MAX_REWARD_DAY = 4
    }
}
