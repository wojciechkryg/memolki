package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.MainDispatcher
import com.wojdor.memolki.domain.usecase.base.BaseParameterUseCase
import com.wojdor.memolki.util.provider.LocaleProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangeLanguageUseCase @Inject constructor(
    @MainDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val localeProvider: LocaleProvider
) : BaseParameterUseCase<String, Unit>(coroutineDispatcher) {

    override fun execute(parameter: String) = flow {
        localeProvider.setLanguageTag(parameter)
        emit(Result.success(Unit))
    }
}
