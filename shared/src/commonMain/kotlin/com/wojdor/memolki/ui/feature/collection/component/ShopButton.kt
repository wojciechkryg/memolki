package com.wojdor.memolki.ui.feature.collection.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.shared.resources.*
import com.wojdor.memolki.ui.component.EdgeSparklesEffect
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.component.shimmerEffect
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.FullRoundedShape
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.ui.theme.spacingXS
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.wojdor.memolki.util.throttleClick

@Composable
fun ShopButton(onClick: () -> Unit = {}) {
    EdgeSparklesEffect(
        modifier = Modifier
            .pulseEffect()
            .bounceClickEffect()
    ) {
        Button(
            onClick = throttleClick(onClick = onClick),
            shape = FullRoundedShape,
            contentPadding = PaddingValues(start = spacingS, end = spacingL),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .clip(FullRoundedShape)
                .shimmerEffect()
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                painter = painterResource(Res.drawable.ic_coins_sack),
                contentDescription = stringResource(Res.string.shop),
            )
            Spacer(modifier = Modifier.size(spacingXS))
            Text(
                text = stringResource(Res.string.shop).uppercase(),
                style = MaterialTheme.typography.titleLarge.animated()
            )
        }
    }
}

@Preview
@Composable
private fun ShopButtonPreview() {
    AppTheme {
        ShopButton()
    }
}
