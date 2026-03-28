package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RewardCoinsForShareUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Boolean>> = flow {
        val result = runCatching {
            val hasReceived = userRepository.getHasReceivedShareReward().first()
            if (!hasReceived) {
                userRepository.addCoins(SHARE_REWARD_COINS)
                userRepository.setHasReceivedShareReward()
                true
            } else {
                false
            }
        }
        emit(result)
    }

    companion object {
        const val SHARE_REWARD_COINS = 3L
    }
}
