package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.CardPairModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetCollectionDataUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val getUnlockedCardPairsUseCase: GetUnlockedCardPairsUseCase,
    private val getAllCardPairsCountUseCase: GetAllCardPairsCountUseCase,
    private val calculateNextCardPairCostUseCase: CalculateNextCardPairCostUseCase,
    private val getUnlockedCardPairsFromAdsCountUseCase: GetUnlockedCardPairsFromAdsCountUseCase
) : BaseUseCase<GetCollectionDataUseCase.CollectionData>(coroutineDispatcher) {

    override fun execute(): Flow<Result<CollectionData>> = combine(
        getUnlockedCardPairsUseCase().map { it.getOrThrow() },
        getAllCardPairsCountUseCase().map { it.getOrThrow() },
        calculateNextCardPairCostUseCase().map { it.getOrThrow() },
        getUnlockedCardPairsFromAdsCountUseCase().map { it.getOrThrow().toInt() }
    ) { unlockedCardPairs, allCardPairsCount, nextCardPairCost, unlockedCardPairsFromAdsCount ->
        Result.success(
            CollectionData(
                unlockedCardPairs = unlockedCardPairs,
                allCardPairsCount = allCardPairsCount,
                nextCardPairCost = nextCardPairCost,
                unlockedCardPairsFromAdsCount = unlockedCardPairsFromAdsCount
            )
        )
    }

    data class CollectionData(
        val unlockedCardPairs: List<CardPairModel>,
        val allCardPairsCount: Int,
        val nextCardPairCost: Int,
        val unlockedCardPairsFromAdsCount: Int
    )
}
