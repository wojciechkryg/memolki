package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class HasAnyDailyChallengeUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val dailyChallengeRepository: DailyChallengeRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(Result.success(dailyChallengeRepository.hasAnyCompleted()))
    }
}
