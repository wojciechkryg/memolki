package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.component.PreviewBackground
import com.wojdor.memolki.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun MenuItemPreview() {
    AppTheme {
        PreviewBackground {
            MenuItem(Res.string.app_logo)
        }
    }
}
