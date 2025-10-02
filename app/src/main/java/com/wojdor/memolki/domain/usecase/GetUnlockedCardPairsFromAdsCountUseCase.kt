package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUnlockedCardPairsFromAdsCountUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Long>(coroutineDispatcher) {

    override fun execute() =
        userRepository.getUnlockedCardPairsFromAdsCount().map { Result.success(it) }
}
