package com.wojdor.memolki.ui.component

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.AppTypography
import com.wojdor.memolki.ui.theme.withColor
import com.wojdor.memolki.ui.util.rememberThrottleClick

@Composable
fun MenuItem(
    @StringRes textId: Int,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = rememberThrottleClick(onClick = onClick),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colorResource(R.color.secondary)
        )
    ) {
        Text(
            text = stringResource(textId).uppercase(),
            style = AppTypography.displaySmall.withColor(colorResource(R.color.text_primary))
        )
    }
}

@Preview
@Composable
fun MenuItemPreview() {
    AppTheme {
        MenuItem(R.string.app_name)
    }
}
