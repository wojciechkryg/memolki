package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetSupportedLanguagesUseCase(
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<List<LanguageModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(
            Result.success(
                listOf(
                    LanguageModel(R.string.language_arabic, "ar"),
                    LanguageModel(R.string.language_czech, "cs"),
                    LanguageModel(R.string.language_danish, "da"),
                    LanguageModel(R.string.language_german, "de"),
                    LanguageModel(R.string.language_greek, "el"),
                    LanguageModel(R.string.language_english, "en"),
                    LanguageModel(R.string.language_spanish, "es"),
                    LanguageModel(R.string.language_estonian, "et"),
                    LanguageModel(R.string.language_finnish, "fi"),
                    LanguageModel(R.string.language_french, "fr"),
                    LanguageModel(R.string.language_hindi, "hi"),
                    LanguageModel(R.string.language_hungarian, "hu"),
                    LanguageModel(R.string.language_indonesian, "in"),
                    LanguageModel(R.string.language_italian, "it"),
                    LanguageModel(R.string.language_hebrew, "iw"),
                    LanguageModel(R.string.language_japanese, "ja"),
                    LanguageModel(R.string.language_korean, "ko"),
                    LanguageModel(R.string.language_lithuanian, "lt"),
                    LanguageModel(R.string.language_latvian, "lv"),
                    LanguageModel(R.string.language_dutch, "nl"),
                    LanguageModel(R.string.language_norwegian, "no"),
                    LanguageModel(R.string.language_polish, "pl"),
                    LanguageModel(R.string.language_portuguese, "pt"),
                    LanguageModel(R.string.language_romanian, "ro"),
                    LanguageModel(R.string.language_russian, "ru"),
                    LanguageModel(R.string.language_slovak, "sk"),
                    LanguageModel(R.string.language_slovenian, "sl"),
                    LanguageModel(R.string.language_swedish, "sv"),
                    LanguageModel(R.string.language_turkish, "tr"),
                    LanguageModel(R.string.language_ukrainian, "uk"),
                    LanguageModel(R.string.language_vietnamese, "vi"),
                    LanguageModel(R.string.language_chinese, "zh")
                )
            )
        )
    }
}
