package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetUnlockedCardPairsCountUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository
) : BaseUseCase<Int>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(Result.success(cardRepository.getUnlockedCardPairs().size))
    }
}
