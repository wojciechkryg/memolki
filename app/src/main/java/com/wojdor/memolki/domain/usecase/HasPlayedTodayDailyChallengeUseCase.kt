package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HasPlayedTodayDailyChallengeUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val timeProvider: TimeProvider
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() = flow {
        val epochDay = timeProvider.currentLocalDate().toEpochDay()
        val lastPlayedEpochDay = dailyChallengeRepository.getLastPlayedEpochDay()
        val isDateRolledBack = lastPlayedEpochDay != null && epochDay < lastPlayedEpochDay
        val hasPlayed = isDateRolledBack || dailyChallengeRepository.hasPlayed(epochDay)
        emit(Result.success(hasPlayed))
    }
}
