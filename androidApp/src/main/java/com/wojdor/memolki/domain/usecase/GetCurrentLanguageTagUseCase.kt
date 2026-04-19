package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.util.provider.LocaleProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetCurrentLanguageTagUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val localeProvider: LocaleProvider
) : BaseUseCase<String>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(Result.success(localeProvider.getLanguageTag()))
    }
}
