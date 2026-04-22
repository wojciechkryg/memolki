package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource

@Composable
fun MenuItem(
    textId: StringResource,
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
        MenuItem(Res.string.app_logo)
    }
}
