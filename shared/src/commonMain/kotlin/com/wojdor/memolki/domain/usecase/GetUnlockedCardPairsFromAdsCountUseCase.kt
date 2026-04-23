package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map

class GetUnlockedCardPairsFromAdsCountUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Long>(coroutineDispatcher) {

    override fun execute() =
        userRepository.getUnlockedCardPairsFromAdsCount().map { Result.success(it) }
}
