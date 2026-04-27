package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.repository.CardRepository
import com.wojdor.memolki.data.repository.DailyChallengeRepository
import com.wojdor.memolki.data.repository.NotificationRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.model.CardModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.domain.usecase.GetBoardsUseCase
import com.wojdor.memolki.domain.usecase.GetDailyChallengeCardsUseCase
import com.wojdor.memolki.domain.usecase.GetShuffledUnlockedCardsUseCase
import com.wojdor.memolki.domain.usecase.GetTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.GetUnlockedCardPairsCountUseCase
import com.wojdor.memolki.domain.usecase.HasPlayedTodayDailyChallengeUseCase
import com.wojdor.memolki.domain.usecase.SaveDailyChallengeUseCase
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

class FakeGetShuffledUnlockedCardsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    cardRepository: CardRepository,
    random: Random
) : GetShuffledUnlockedCardsUseCase(coroutineDispatcher, cardRepository, random) {
    var result: Result<List<CardModel>> = Result.success(emptyList())
    override fun execute(board: BoardModel): Flow<Result<List<CardModel>>> = flowOf(result)
}

class FakeGetDailyChallengeCardsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    cardRepository: CardRepository,
    timeProvider: TimeProvider,
    packageNameProvider: PackageNameProvider
) : GetDailyChallengeCardsUseCase(coroutineDispatcher, cardRepository, timeProvider, packageNameProvider) {
    var result: Result<List<CardModel>> = Result.success(emptyList())
    override fun execute(board: BoardModel): Flow<Result<List<CardModel>>> = flowOf(result)
}

class FakeHasPlayedTodayDailyChallengeUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    dailyChallengeRepository: DailyChallengeRepository,
    timeProvider: TimeProvider
) : HasPlayedTodayDailyChallengeUseCase(coroutineDispatcher, dailyChallengeRepository, timeProvider) {
    var result: Result<Boolean> = Result.success(false)
    var invocationCount: Int = 0
        private set

    override fun execute(): Flow<Result<Boolean>> {
        invocationCount++
        return flowOf(result)
    }
}

class FakeSaveDailyChallengeUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    dailyChallengeRepository: DailyChallengeRepository,
    notificationRepository: NotificationRepository,
    timeProvider: TimeProvider
) : SaveDailyChallengeUseCase(coroutineDispatcher, dailyChallengeRepository, notificationRepository, timeProvider) {
    var result: Result<Unit> = Result.success(Unit)
    var lastSavedModel: DailyChallengeModel? = null
        private set

    override fun execute(parameter: DailyChallengeModel): Flow<Result<Unit>> {
        lastSavedModel = parameter
        return flowOf(result)
    }
}

class FakeGetTodayDailyChallengeUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    dailyChallengeRepository: DailyChallengeRepository,
    timeProvider: TimeProvider
) : GetTodayDailyChallengeUseCase(coroutineDispatcher, dailyChallengeRepository, timeProvider) {
    var result: Result<DailyChallengeModel> = Result.failure(IllegalStateException("not configured"))
    override fun execute(): Flow<Result<DailyChallengeModel>> = flowOf(result)
}

class FakeGetBoardsUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    getUnlockedCardPairsCountUseCase: GetUnlockedCardPairsCountUseCase
) : GetBoardsUseCase(coroutineDispatcher, getUnlockedCardPairsCountUseCase) {
    var result: Result<List<BoardModel>> = Result.success(emptyList())
    override fun execute(): Flow<Result<List<BoardModel>>> = flowOf(result)
}

class FakeGetUnlockedCardPairsCountUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    cardRepository: CardRepository
) : GetUnlockedCardPairsCountUseCase(coroutineDispatcher, cardRepository) {
    var result: Result<Int> = Result.success(0)
    override fun execute(): Flow<Result<Int>> = flowOf(result)
}
