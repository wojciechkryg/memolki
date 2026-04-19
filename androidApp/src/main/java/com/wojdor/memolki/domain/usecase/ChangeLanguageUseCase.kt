package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.provider.LocaleProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class ChangeLanguageUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val localeProvider: LocaleProvider
) : BaseParameterUseCase<String, Unit>(coroutineDispatcher) {

    override fun execute(parameter: String) = flow {
        localeProvider.setLanguageTag(parameter)
        emit(Result.success(Unit))
    }
}
