package com.wojdor.memolki.ui.feature.moreapps.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.component.AutoSizeText
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.util.throttleClick

@Composable
fun AppItem(
    modifier: Modifier = Modifier,
    appModel: AppModel,
    onClick: (AppModel) -> Unit = {}
) {
    CardBorder(modifier = modifier) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = throttleClick(onClick = { onClick(appModel) }),
            shape = CardShape,
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(appModel.colorRes),
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Row(
                modifier.height(120.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.height(72.dp),
                    painter = painterResource(appModel.imageRes),
                    contentDescription = null
                )
                AutoSizeText(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(appModel.textRes),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppItemPreview() {
    AppTheme {
        AppItem(
            modifier = Modifier.width(320.dp),
            appModel = AppModel.VegetableHalf
        )
    }
}
