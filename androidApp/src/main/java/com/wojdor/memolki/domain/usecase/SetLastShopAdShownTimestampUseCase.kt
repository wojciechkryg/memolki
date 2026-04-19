package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.UserRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class SetLastShopAdShownTimestampUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : BaseUseCase<Unit>(coroutineDispatcher) {

    override fun execute() = flow {
        userRepository.setLastShopAdShownTimestamp(System.currentTimeMillis())
        emit(Result.success(Unit))
    }
}
