package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.util.rememberThrottleClick

@Composable
fun ShopButton(onClick: () -> Unit = {}) {
    IconButton(
        modifier = Modifier.padding(16.dp),
        onClick = rememberThrottleClick(onClick = onClick),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = colorResource(R.color.font)
        )
    }
}

@Preview
@Composable
private fun ShopButtonPreview() {
    ShopButton()
}
