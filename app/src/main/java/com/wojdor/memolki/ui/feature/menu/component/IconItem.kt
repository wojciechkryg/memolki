package com.wojdor.memolki.ui.feature.menu.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingXS
import com.wojdor.memolki.util.throttleClick

@Composable
fun IconItem(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = throttleClick(onClick),
        contentPadding = PaddingValues(spacingXS),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Icon(
            modifier = Modifier.size(96.dp),
            painter = painterResource(iconRes),
            contentDescription = null
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
