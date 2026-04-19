package com.wojdor.memolki.ui.feature.menu.component

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun MenuItem(
    @StringRes textId: Int,
    onClick: () -> Unit = {}
) {
    BaseMenuItem(
        textId = textId,
        textStyle = MaterialTheme.typography.displayMedium,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuItemPreview() {
    AppTheme {
        MenuItem(R.string.app_name)
    }
}
