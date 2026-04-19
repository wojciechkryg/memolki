package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IncrementLevelUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseParameterUseCase<String, Long>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(boardId: String): Flow<Result<Long>> = flow {
        val level = userRepository.incrementLevel(boardId)
        emit(Result.success(level))
    }
}
