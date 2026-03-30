package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.ui.component.RECORDING_MODE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PrepareRecordingCoinsUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Unit>> = flow {
        emit(runCatching {
            if (!RECORDING_MODE) return@runCatching
            val currentCoins = userRepository.getCoins().first()
            if (currentCoins == 0L) {
                userRepository.addCoins(RECORDING_MODE_INITIAL_COINS)
            }
        })
    }

    companion object {
        private const val RECORDING_MODE_INITIAL_COINS = 473L
    }
}
