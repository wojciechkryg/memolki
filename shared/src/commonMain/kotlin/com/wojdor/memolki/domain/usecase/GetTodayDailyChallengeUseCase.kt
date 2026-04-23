package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetTodayDailyChallengeUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val timeProvider: TimeProvider
) : BaseUseCase<DailyChallengeModel>(coroutineDispatcher) {

    override fun execute() = flow {
        val epochDay = timeProvider.currentLocalDate().toEpochDays()
        val result = dailyChallengeRepository.getResult(epochDay)
            ?: throw IllegalStateException("No daily challenge entry for epoch day $epochDay")
        emit(Result.success(result))
    }
}
