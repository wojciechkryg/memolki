package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCardPairsUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val cardRepository: CardRepository
) : BaseUseCase<List<CardPairModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(Result.success(cardRepository.getAllCardPairs()))
    }
}
