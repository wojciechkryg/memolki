package com.wojdor.memolki.ui.components

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
import com.wojdor.memolki.util.rememberThrottleClick

@Composable
fun BaseMenuItem(
    @StringRes textId: Int,
    isUppercase: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = rememberThrottleClick(onClick = onClick),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = stringResource(textId).run {
                if (isUppercase) {
                    uppercase()
                } else {
                    lowercase()
                }
            },
            style = AppTypography.displaySmall.withColor(colorResource(R.color.font))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseMenuItemUppercasePreview() {
    AppTheme {
        BaseMenuItem(R.string.app_name)
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseMenuItemLowercasePreview() {
    AppTheme {
        BaseMenuItem(R.string.app_name, isUppercase = false)
    }
}
