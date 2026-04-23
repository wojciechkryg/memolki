package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetAllDailyChallengesUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val dailyChallengeRepository: DailyChallengeRepository
) : BaseUseCase<List<DailyChallengeModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        val results = dailyChallengeRepository.getAll()
        emit(Result.success(results))
    }
}
