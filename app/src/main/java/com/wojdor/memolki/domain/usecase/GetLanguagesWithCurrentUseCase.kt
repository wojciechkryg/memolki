package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.di.coroutine.IoDispatcher
import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetLanguagesWithCurrentUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val getSupportedLanguagesUseCase: GetSupportedLanguagesUseCase,
    private val getCurrentLanguageTagUseCase: GetCurrentLanguageTagUseCase
) : BaseUseCase<Pair<List<LanguageModel>, LanguageModel>>(coroutineDispatcher) {

    override fun execute(): Flow<Result<Pair<List<LanguageModel>, LanguageModel>>> =
        combine(
            getSupportedLanguagesUseCase(),
            getCurrentLanguageTagUseCase()
        ) { languagesResult, tagResult ->
            val languages = languagesResult.getOrThrow()
            val currentTag = tagResult.getOrThrow()
            val currentLanguage = languages.firstOrNull { it.tag == currentTag }
                ?: languages.first { it.tag == DEFAULT_LANGUAGE_TAG }
            Result.success(languages to currentLanguage)
        }

    companion object {
        private const val DEFAULT_LANGUAGE_TAG = "en"
    }
}
