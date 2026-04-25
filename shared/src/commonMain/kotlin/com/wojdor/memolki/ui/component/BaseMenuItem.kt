package com.wojdor.memolki.ui.component

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.app_logo
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.throttleClick
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BaseMenuItem(
    textId: StringResource,
    isEnabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.displaySmall,
    alpha: Float = 1f,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = (if (isEnabled) Modifier.bounceClickEffect() else Modifier).alpha(alpha),
        onClick = throttleClick(onClick = onClick),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = isEnabled
    ) {
        Text(
            text = stringResource(textId).lowercase(),
            style = textStyle
        )
    }
}

@Preview
@Composable
private fun BaseMenuItemPreview() {
    AppTheme {
        PreviewBackground {
            BaseMenuItem(Res.string.app_logo)
        }
    }
}

@Preview
@Composable
private fun BaseMenuItemDisabledPreview() {
    AppTheme {
        PreviewBackground {
            BaseMenuItem(Res.string.app_logo, isEnabled = false)
        }
    }
}
