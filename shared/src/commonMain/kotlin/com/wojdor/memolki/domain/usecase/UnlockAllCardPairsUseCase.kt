package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class UnlockAllCardPairsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        cardRepository.unlockAllCardPairs()
        emit(Result.success(Unit))
    }
}
