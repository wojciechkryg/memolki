package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RewardCoinsForShopAdUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val calculateCoinsForShopAdUseCase: CalculateCoinsForShopAdUseCase
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        val result = calculateCoinsForShopAdUseCase().first()
        result.getOrNull()?.let {
            userRepository.addCoins(it)
            emit(Result.success(Unit))
        } ?: emit(Result.failure(Exception("Failed to calculate reward coins for ad")))
    }
}
