package com.wojdor.memolki.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.formatter.TimeFormatter
import org.jetbrains.compose.ui.tooling.preview.Preview

private val timeFormatter = TimeFormatter()

@Composable
fun TimeDisplay(
    modifier: Modifier = Modifier,
    timeMillis: Long,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    millisTextStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val formatted = timeFormatter.format(timeMillis)
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(formatted.main)
            withStyle(SpanStyle(fontSize = millisTextStyle.fontSize)) {
                append(formatted.millis)
            }
        },
        style = textStyle
    )
}

@Preview
@Composable
private fun TimeDisplayPreview() {
    AppTheme {
        PreviewBackground {
            TimeDisplay(timeMillis = 83456L)
        }
    }
}
