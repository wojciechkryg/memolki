package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.di.coroutine.DefaultDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetMusicEnabledUseCase @Inject constructor(
    @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val settingsRepository: SettingsRepository
) : BaseParameterUseCase<Boolean, Unit>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(isEnabled: Boolean) = flow {
        settingsRepository.setMusicEnabled(isEnabled)
        emit(Result.success(Unit))
    }
}
