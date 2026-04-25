package com.wojdor.memolki.ui.feature.game.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import com.wojdor.memolki.ui.theme.AppColors
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardBorder(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(CARD_BORDER_SIZE, AppColors.Border),
                shape = CardShape
            )
            .clip(CardShape),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

val CARD_BORDER_SIZE = 2.dp

@Preview
@Composable
private fun CardBorderPreview() {
    AppTheme {
        CardBorder(Modifier.size(128.dp)) {
            Text(text = stringResource(Res.string.app_logo))
        }
    }
}
