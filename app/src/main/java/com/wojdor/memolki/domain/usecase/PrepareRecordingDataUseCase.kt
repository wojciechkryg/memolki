package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PrepareRecordingDataUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        @Suppress("KotlinConstantConditions")
        if (!RECORDING_MODE) {
            emit(Result.success(Unit))
            return@flow
        }
        val currentCoins = userRepository.getCoins().first()
        if (currentCoins == 0L) {
            userRepository.addCoins(473L)
        }
        userRepository.setLevel(BoardModel.Grid3x4().id, 13L)
        userRepository.setLevel(BoardModel.Grid5x6().id, 78L)
        emit(Result.success(Unit))
    }
}
