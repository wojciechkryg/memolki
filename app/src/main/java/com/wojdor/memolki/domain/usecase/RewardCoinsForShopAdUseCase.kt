package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RewardCoinsForShopAdUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val getLevelsUseCase: GetLevelsUseCase
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        val rewardedCoins = calculateRewardedCoins()
        userRepository.addCoins(rewardedCoins)
        emit(Result.success(Unit))
    }

    private suspend fun calculateRewardedCoins(): Long {
        val unlockedLevels = getLevelsUseCase().first().getOrNull() ?: return 0
        val biggestUnlockedLevel = unlockedLevels.filter { it.isUnlocked }.maxByOrNull { it.id }
        return biggestUnlockedLevel?.let {
            it.columns * it.rows.toLong()
        } ?: 0
    }
}
