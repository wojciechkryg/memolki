package com.wojdor.memolki.ui.feature.menu.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.throttleClick

@Composable
fun IconItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    size: Dp = 96.dp,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .bounceClickEffect()
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = throttleClick(onClick)
            )
            .padding(spacingS),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(iconRes),
            contentDescription = contentDescription
        )
    }
}


@Preview
@Composable
private fun IconItemPreview() {
    AppTheme {
        IconItem(iconRes = R.drawable.ic_leaderboard)
    }
}
