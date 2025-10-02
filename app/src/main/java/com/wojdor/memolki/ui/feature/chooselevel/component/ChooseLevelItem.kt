package com.wojdor.memolki.ui.feature.chooselevel.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ChooseLevelItem(
    @StringRes textId: Int,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (!isEnabled) {
            Icon(
                modifier = Modifier.size(LOCKED_ICON_SIZE),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = colorResource(R.color.font)
            )
        }
        BaseMenuItem(
            textId = textId,
            isUppercase = false,
            isEnabled = isEnabled,
            onClick = onClick
        )
        if (!isEnabled) {
            Spacer(modifier = Modifier.size(LOCKED_ICON_SIZE))
        }
    }
}

private val LOCKED_ICON_SIZE = 48.dp

@Preview(showBackground = true)
@Composable
private fun ChooseLevelItemDisabledPreview() {
    AppTheme {
        ChooseLevelItem(R.string.level2x3, isEnabled = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseLevelItemEnabledPreview() {
    AppTheme {
        ChooseLevelItem(R.string.level2x3, isEnabled = true)
    }
}
