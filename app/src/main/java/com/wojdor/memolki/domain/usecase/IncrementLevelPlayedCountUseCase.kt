package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IncrementLevelPlayedCountUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseParameterUseCase<String, Long>(coroutineDispatcher) {

    override fun execute(parameter: String): Flow<Result<Long>> = flow {
        val newCount = userRepository.incrementLevelPlayedCount(parameter)
        emit(Result.success(newCount))
    }
}
