package com.wojdor.memolki.ui.feature.chooselevel.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.components.BaseMenuItem
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ChooseLevelItem(
    @StringRes textId: Int,
    onClick: () -> Unit = {}
) {
    BaseMenuItem(
        textId = textId,
        isUppercase = false,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuItemPreview() {
    AppTheme {
        ChooseLevelItem(R.string.app_name)
    }
}
