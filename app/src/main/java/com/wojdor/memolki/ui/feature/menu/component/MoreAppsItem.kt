package com.wojdor.memolki.ui.feature.menu.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.component.bounceClickEffect
import com.wojdor.memolki.ui.component.pulseEffect
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.animated
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.ui.theme.spacingS
import com.wojdor.memolki.util.throttleClick

@Composable
fun MoreAppsItem(
    modifier: Modifier = Modifier, appModel: AppModel, onClick: () -> Unit = {}
) {
    Column(modifier = modifier.padding(spacingL)) {
        Text(
            modifier = Modifier.padding(start = spacingS),
            text = stringResource(R.string.more).lowercase(),
            style = MaterialTheme.typography.headlineLarge
        )
        CardBorder(modifier = Modifier
            .pulseEffect()
            .bounceClickEffect()) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = throttleClick(onClick),
                shape = CardShape,
                contentPadding = PaddingValues(spacingM),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(appModel.colorRes),
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.height(48.dp),
                        painter = painterResource(appModel.imageRes),
                        contentDescription = stringResource(appModel.textRes)
                    )
                    Text(
                        modifier = Modifier.padding(start = spacingS),
                        text = stringResource(appModel.textRes),
                        style = MaterialTheme.typography.headlineMedium.animated()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoreAppsItemPreview() {
    AppTheme {
        MoreAppsItem(
            modifier = Modifier.size(width = 360.dp, height = 240.dp),
            appModel = AppModel.VegetableHalf
        )
    }
}
