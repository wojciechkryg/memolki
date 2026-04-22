package com.wojdor.memolki.ui.feature.chooseboard.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.component.ForceLtr
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.rememberShakeOffset
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CheckboxShape
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.util.throttleClick

@Composable
fun DailyChallengeItem(
    isCompleted: Boolean = false,
    showHistoryIcon: Boolean = false,
    onClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    var isShaking by remember { mutableStateOf(false) }
    val shakeOffset = rememberShakeOffset(isShaking) { isShaking = false }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .graphicsLayer { translationX = shakeOffset }
                .then(
                    if (isCompleted) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForceLtr {
                Button(
                    modifier = if (!isCompleted) Modifier.bounceClickEffect() else Modifier,
                    onClick = throttleClick(onClick = onClick),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    enabled = !isCompleted
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DailyChallengeCheckbox(isCompleted)
                            Spacer(modifier = Modifier.width(spacingL))
                            Text(
                                text = stringResource(Res.string.daily_challenge).lowercase(),
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                        if (isCompleted) {
                            Text(
                                text = stringResource(Res.string.daily_reward_back_tomorrow).lowercase(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
        if (showHistoryIcon) {
            HistoryButton(onHistoryClick)
        }
    }
}

@Composable
private fun DailyChallengeCheckbox(isCompleted: Boolean) {
    Box(
        modifier = Modifier.size(CHECKBOX_SIZE),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(CHECKBOX_SIZE)
                .border(
                    border = BorderStroke(2.dp, colorResource(R.color.border)),
                    shape = CheckboxShape
                )
        )
        if (isCompleted) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .size(56.dp)
                    .offset(x = 6.dp, y = (-12).dp)
            )
        }
    }
}

@Composable
private fun HistoryButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.bounceClickEffect(),
        onClick = throttleClick(onClick = onClick),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_history),
            contentDescription = null,
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(spacingL))
        Text(
            text = stringResource(Res.string.daily_challenge_history).lowercase(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private val CHECKBOX_SIZE = 24.dp

@Preview(showBackground = true)
@Composable
private fun DailyChallengeItemAvailablePreview() {
    AppTheme {
        DailyChallengeItem(isCompleted = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeItemCompletedPreview() {
    AppTheme {
        DailyChallengeItem(isCompleted = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeItemWithHistoryPreview() {
    AppTheme {
        DailyChallengeItem(isCompleted = false, showHistoryIcon = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeItemCompletedWithHistoryPreview() {
    AppTheme {
        DailyChallengeItem(isCompleted = true, showHistoryIcon = true)
    }
}
