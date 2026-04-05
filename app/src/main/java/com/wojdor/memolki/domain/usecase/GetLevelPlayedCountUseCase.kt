package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLevelPlayedCountUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseParameterUseCase<String, Long>(coroutineDispatcher) {

    override fun execute(parameter: String): Flow<Result<Long>> {
        return userRepository.getLevelPlayedCount(parameter)
            .map { Result.success(it) }
    }
}
