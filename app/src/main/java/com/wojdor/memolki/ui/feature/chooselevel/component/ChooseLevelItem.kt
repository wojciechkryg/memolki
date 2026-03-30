package com.wojdor.memolki.ui.feature.chooselevel.component

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.component.ForceLtr
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.component.rememberShakeOffset
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun ChooseLevelItem(
    @StringRes textId: Int,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    var isShaking by remember { mutableStateOf(false) }
    val shakeOffset = rememberShakeOffset(isShaking) { isShaking = false }
    Row(
        modifier = Modifier
            .graphicsLayer { translationX = shakeOffset }
            .then(
                if (!isEnabled) {
                    Modifier.pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            isShaking = true
                        }
                    }
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isEnabled) {
            Icon(
                modifier = Modifier.size(LOCKED_ICON_SIZE),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = stringResource(R.string.content_description_locked)
            )
        }
        ForceLtr {
            BaseMenuItem(
                textId = textId,
                isUppercase = false,
                isEnabled = isEnabled,
                onClick = onClick
            )
        }
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
