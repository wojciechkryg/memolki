package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.provider.AppInstalledProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class IsAppInstalledUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val appInstalledProvider: AppInstalledProvider
) : BaseParameterUseCase<String, Boolean>(coroutineDispatcher) {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(appId: String) = flow {
        val isAppInstalled = appInstalledProvider.isAppInstalled(appId)
        emit(Result.success(isAppInstalled))
    }
}
