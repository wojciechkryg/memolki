package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.component.rememberShakeOffset
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.isLargeScreen
import com.wojdor.memolki.ui.theme.spacingL

@Composable
fun CollectionLockedCardPair(modifier: Modifier = Modifier) {
    var isShaking by remember { mutableStateOf(false) }
    val shakeOffset = rememberShakeOffset(isShaking) { isShaking = false }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isLargeScreen) spacingL else 0.dp)
            .graphicsLayer { translationX = shakeOffset }
            .clip(rotatedCardPairShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { isShaking = true }
            .padding(vertical = CARD_PAIR_VERTICAL_PADDING),
        contentAlignment = Alignment.Center
    ) {
        CollectionLockedCard(modifier = cardLeftModifier)
        Box(
            modifier = cardRightModifier,
            contentAlignment = Alignment.Center
        ) {
            CollectionLockedCard()
            Icon(
                modifier = Modifier.size(LOCKED_ICON_SIZE),
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = stringResource(Res.string.content_description_locked)
            )
        }
    }
}

private val LOCKED_ICON_SIZE = 48.dp

@Preview
@Composable
fun CollectionLockedCardPairPreview() {
    AppTheme {
        CollectionLockedCardPair(Modifier.width(192.dp))
    }
}
