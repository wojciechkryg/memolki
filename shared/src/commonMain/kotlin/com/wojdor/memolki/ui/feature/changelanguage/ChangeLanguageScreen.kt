package com.wojdor.memolki.ui.feature.changelanguage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.language_arabic
import com.wojdor.memolki.shared.resources.language_czech
import com.wojdor.memolki.shared.resources.language_danish
import com.wojdor.memolki.shared.resources.language_english
import com.wojdor.memolki.shared.resources.language_estonian
import com.wojdor.memolki.shared.resources.language_german
import com.wojdor.memolki.shared.resources.language_greek
import com.wojdor.memolki.shared.resources.language_spanish
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.FadeEffectBottom
import com.wojdor.memolki.ui.component.FadeEffectTop
import com.wojdor.memolki.ui.component.FullScreenOverlay
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.feature.changelanguage.component.LanguageButton
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingM
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangeLanguageScreen(
    viewModel: ChangeLanguageViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: ChangeLanguageViewModel,
    navController: NavController
) {
    val wasChangingOnStart = remember { viewModel.uiState.value.isLanguageChangeInProgress }
    if (wasChangingOnStart) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            is ChangeLanguageEffect.NavigateBack -> navController.popBackStack()
        }
    }
}

@Composable
private fun HandleState(
    viewModel: ChangeLanguageViewModel,
    state: ChangeLanguageState
) {
    val callbacks = ChangeLanguageCallbacks(
        onLanguageChange = {
            viewModel.sendIntent(ChangeLanguageIntent.OnLanguageClick(it))
        },
        onLanguageChangeReady = {
            viewModel.sendIntent(ChangeLanguageIntent.OnLanguageChangeReady)
        }
    )
    ChangeLanguageScreen(state, callbacks)
}

@Composable
private fun ChangeLanguageScreen(
    state: ChangeLanguageState,
    callbacks: ChangeLanguageCallbacks = ChangeLanguageCallbacks()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacingM),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.languages) { language ->
                LanguageButton(
                    language = language,
                    isSelected = language.tag == state.currentLanguage.tag,
                    onClick = { callbacks.onLanguageChange(language) }
                )
            }
        }
        FadeEffectTop(Modifier.align(Alignment.TopCenter))
        FadeEffectBottom(Modifier.align(Alignment.BottomCenter))
        FullScreenOverlay(
            isVisible = state.isLanguageChangeInProgress,
            onShown = callbacks.onLanguageChangeReady
        )
    }
}

@Preview
@Composable
private fun ChangeLanguageScreenPreview() {
    AppTheme {
        PreviewBackground {
            val currentLanguage = LanguageModel(Res.string.language_english, "en")
            ChangeLanguageScreen(
                state = ChangeLanguageState(
                    languages = listOf(
                        LanguageModel(Res.string.language_arabic, "ar"),
                        LanguageModel(Res.string.language_czech, "cs"),
                        LanguageModel(Res.string.language_danish, "da"),
                        LanguageModel(Res.string.language_german, "de"),
                        LanguageModel(Res.string.language_greek, "el"),
                        currentLanguage,
                        LanguageModel(Res.string.language_spanish, "es"),
                        LanguageModel(Res.string.language_estonian, "et")
                    ),
                    currentLanguage = currentLanguage
                )
            )
        }
    }
}
