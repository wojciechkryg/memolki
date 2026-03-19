package com.wojdor.memolki.ui.feature.changelanguage.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.LanguageModel
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXS
import com.wojdor.memolki.util.throttleClick

@Composable
fun LanguageButton(
    language: LanguageModel,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier.bounceClickEffect().semantics {
            role = Role.RadioButton
            selected = isSelected
        },
        onClick = throttleClick(onClick = onClick),
        contentPadding = if (isSelected) PaddingValues(
            top = spacingS,
            bottom = spacingS,
            start = spacingS,
            end = spacingL
        ) else ButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
            ) {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = spacingXS),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(id = language.textId).uppercase(),
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

@Preview
@Composable
private fun LanguageButtonSelectedPreview() {
    AppTheme {
        LanguageButton(
            language = LanguageModel(R.string.language_english, "en"),
            isSelected = true
        )
    }
}

@Preview
@Composable
private fun LanguageButtonPreview() {
    AppTheme {
        LanguageButton(
            language = LanguageModel(R.string.language_english, "en"),
            isSelected = false
        )
    }
}
