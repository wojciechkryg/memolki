package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class IncrementTotalCardPairsMatchedUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Long>(coroutineDispatcher) {

    override fun execute() = flow {
        val totalCardPairsMatched = userRepository.incrementTotalCardPairsMatched()
        emit(Result.success(totalCardPairsMatched))
    }
}
