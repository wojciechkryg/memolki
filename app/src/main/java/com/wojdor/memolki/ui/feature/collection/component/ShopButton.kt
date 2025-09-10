package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.util.rememberThrottleClick

@Composable
fun ShopButton(onClick: () -> Unit = {}) {
    Button(
        modifier = Modifier
            .padding(16.dp)
            .size(64.dp),
        onClick = rememberThrottleClick(onClick = onClick),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent
        ),
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_shop),
            tint = colorResource(id = R.color.font),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun ShopButtonPreview() {
    ShopButton()
}
