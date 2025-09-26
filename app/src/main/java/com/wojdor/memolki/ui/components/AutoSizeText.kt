package com.wojdor.memolki.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.extension.containsWhitespace
import kotlin.math.min
import kotlin.math.sqrt

@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    var scaledTextStyle by remember(text, style) { mutableStateOf(style) }
    var isReadyToDraw by remember(text, style) { mutableStateOf(false) }
    var isSoftWrap by remember(text, style) { mutableStateOf(text.containsWhitespace) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            Modifier.drawWithContent {
                if (isReadyToDraw) {
                    drawContent()
                }
            },
            style = scaledTextStyle,
            softWrap = isSoftWrap,
            textAlign = TextAlign.Center,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    val widthScale =
                        textLayoutResult.size.width / textLayoutResult.multiParagraph.width
                    val heightScale =
                        textLayoutResult.size.height / textLayoutResult.multiParagraph.height
                    if (widthScale.isNaN() || heightScale.isNaN()) {
                        return@Text
                    }
                    val scale = if (isSoftWrap) {
                        sqrt(min(widthScale, heightScale) * SOFT_WRAP_SCALE_FACTOR)
                    } else {
                        min(widthScale, heightScale)
                    }
                    scaledTextStyle =
                        scaledTextStyle.copy(
                            fontSize = scaledTextStyle.fontSize * scale,
                            lineHeight = scaledTextStyle.lineHeight * scale,
                        )
                } else {
                    isReadyToDraw = true
                }
            }
        )
    }
}

private const val SOFT_WRAP_SCALE_FACTOR = 0.95f

// Preview doesn't work in Android Studio, but it can be rendered properly on the device
@Preview
@Composable
private fun AutoSizeTextBigPreview() {
    AppTheme {
        AutoSizeText(
            text = "Lorem ipsum",
            modifier = Modifier.size(128.dp),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

// Preview doesn't work in Android Studio, but it can be rendered properly on the device
@Preview
@Composable
private fun AutoSizeTextMediumPreview() {
    AppTheme {
        AutoSizeText(
            text = "Lorem ipsum dolor sit amet",
            modifier = Modifier.size(128.dp),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

// Preview doesn't work in Android Studio, but it can be rendered properly on the device
@Preview
@Composable
private fun AutoSizeTextSmallPreview() {
    AppTheme {
        AutoSizeText(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            modifier = Modifier.size(128.dp),
            style = MaterialTheme.typography.displayLarge
        )
    }
}
