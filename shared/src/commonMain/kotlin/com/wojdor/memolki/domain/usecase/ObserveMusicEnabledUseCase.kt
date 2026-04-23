package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.data.repository.SettingsRepository
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ObserveMusicEnabledUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val settingsRepository: SettingsRepository
) : BaseUseCase<Boolean>(coroutineDispatcher) {

    override fun execute() =
        settingsRepository.getMusicEnabled()
            .distinctUntilChanged()
            .map { Result.success(it) }
}
