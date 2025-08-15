package com.wojdor.memolki.domain.usecase.base

import com.wojdor.memolki.ui.util.extension.logE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class BaseParameterUseCase<P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    operator fun invoke(parameter: P): Flow<Result<R>> = execute(parameter)
        .catch {
            logE(it.message ?: "No error message", it)
            emit(Result.failure(it))
        }.flowOn(coroutineDispatcher)

    protected abstract fun execute(parameter: P): Flow<Result<R>>
}
