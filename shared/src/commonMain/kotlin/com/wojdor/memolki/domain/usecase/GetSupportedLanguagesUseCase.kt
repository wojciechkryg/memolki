package com.wojdor.memolki.domain.usecase

import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.domain.usecase.base.BaseUseCase
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.language_arabic
import com.wojdor.memolki.shared.resources.language_chinese
import com.wojdor.memolki.shared.resources.language_czech
import com.wojdor.memolki.shared.resources.language_danish
import com.wojdor.memolki.shared.resources.language_dutch
import com.wojdor.memolki.shared.resources.language_english
import com.wojdor.memolki.shared.resources.language_estonian
import com.wojdor.memolki.shared.resources.language_finnish
import com.wojdor.memolki.shared.resources.language_french
import com.wojdor.memolki.shared.resources.language_german
import com.wojdor.memolki.shared.resources.language_greek
import com.wojdor.memolki.shared.resources.language_hebrew
import com.wojdor.memolki.shared.resources.language_hindi
import com.wojdor.memolki.shared.resources.language_hungarian
import com.wojdor.memolki.shared.resources.language_indonesian
import com.wojdor.memolki.shared.resources.language_italian
import com.wojdor.memolki.shared.resources.language_japanese
import com.wojdor.memolki.shared.resources.language_korean
import com.wojdor.memolki.shared.resources.language_latvian
import com.wojdor.memolki.shared.resources.language_lithuanian
import com.wojdor.memolki.shared.resources.language_norwegian
import com.wojdor.memolki.shared.resources.language_polish
import com.wojdor.memolki.shared.resources.language_portuguese
import com.wojdor.memolki.shared.resources.language_romanian
import com.wojdor.memolki.shared.resources.language_russian
import com.wojdor.memolki.shared.resources.language_slovak
import com.wojdor.memolki.shared.resources.language_slovenian
import com.wojdor.memolki.shared.resources.language_spanish
import com.wojdor.memolki.shared.resources.language_swedish
import com.wojdor.memolki.shared.resources.language_turkish
import com.wojdor.memolki.shared.resources.language_ukrainian
import com.wojdor.memolki.shared.resources.language_vietnamese
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow

class GetSupportedLanguagesUseCase(
    coroutineDispatcher: CoroutineDispatcher
) : BaseUseCase<List<LanguageModel>>(coroutineDispatcher) {

    override fun execute() = flow {
        emit(
            Result.success(
                listOf(
                    LanguageModel(Res.string.language_arabic, "ar"),
                    LanguageModel(Res.string.language_czech, "cs"),
                    LanguageModel(Res.string.language_danish, "da"),
                    LanguageModel(Res.string.language_german, "de"),
                    LanguageModel(Res.string.language_greek, "el"),
                    LanguageModel(Res.string.language_english, "en"),
                    LanguageModel(Res.string.language_spanish, "es"),
                    LanguageModel(Res.string.language_estonian, "et"),
                    LanguageModel(Res.string.language_finnish, "fi"),
                    LanguageModel(Res.string.language_french, "fr"),
                    LanguageModel(Res.string.language_hindi, "hi"),
                    LanguageModel(Res.string.language_hungarian, "hu"),
                    LanguageModel(Res.string.language_indonesian, "in"),
                    LanguageModel(Res.string.language_italian, "it"),
                    LanguageModel(Res.string.language_hebrew, "iw"),
                    LanguageModel(Res.string.language_japanese, "ja"),
                    LanguageModel(Res.string.language_korean, "ko"),
                    LanguageModel(Res.string.language_lithuanian, "lt"),
                    LanguageModel(Res.string.language_latvian, "lv"),
                    LanguageModel(Res.string.language_dutch, "nl"),
                    LanguageModel(Res.string.language_norwegian, "no"),
                    LanguageModel(Res.string.language_polish, "pl"),
                    LanguageModel(Res.string.language_portuguese, "pt"),
                    LanguageModel(Res.string.language_romanian, "ro"),
                    LanguageModel(Res.string.language_russian, "ru"),
                    LanguageModel(Res.string.language_slovak, "sk"),
                    LanguageModel(Res.string.language_slovenian, "sl"),
                    LanguageModel(Res.string.language_swedish, "sv"),
                    LanguageModel(Res.string.language_turkish, "tr"),
                    LanguageModel(Res.string.language_ukrainian, "uk"),
                    LanguageModel(Res.string.language_vietnamese, "vi"),
                    LanguageModel(Res.string.language_chinese, "zh")
                )
            )
        )
    }
}
