package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PrepareRecordingDataUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        if (!RECORDING_MODE) {
            emit(Result.success(Unit))
            return@flow
        }
        val currentCoins = userRepository.getCoins().first()
        if (currentCoins == 0L) {
            userRepository.addCoins(473L)
        }
        val totalGamesPlayed = userRepository.getTotalGamesPlayed().first()
        if (totalGamesPlayed == 0L) {
            userRepository.incrementTotalGamesPlayed()
        }
        userRepository.setLevel(BoardModel.Grid3x4().id, 13L)
        userRepository.setLevel(BoardModel.Grid5x6().id, 78L)
        emit(Result.success(Unit))
    }
}
