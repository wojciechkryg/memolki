package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVibrationsEnabledUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val settingsRepository: SettingsRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(Result.success(settingsRepository.getVibrationsEnabled()))
    }
}
