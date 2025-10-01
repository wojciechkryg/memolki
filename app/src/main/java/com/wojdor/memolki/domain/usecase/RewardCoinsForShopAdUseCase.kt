package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
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

    private fun calculateRewardedCoins(): Long {
        // TODO: Use getLevelsUseCase
        return 10
    }
}
