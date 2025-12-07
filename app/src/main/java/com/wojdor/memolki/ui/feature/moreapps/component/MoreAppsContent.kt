package com.wojdor.memolki.ui.feature.moreapps.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.ui.component.FadeEffectBottom
import com.wojdor.memolki.ui.component.FadeEffectTop
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsCallbacks
import com.wojdor.memolki.ui.feature.moreapps.MoreAppsState
import com.wojdor.memolki.ui.theme.AppTheme

@Composable
fun MoreAppsContent(
    state: MoreAppsState,
    callbacks: MoreAppsCallbacks
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.apps) {
                AppItem(appModel = it, onClick = callbacks.onAppClick)
            }
        }
        FadeEffectTop(Modifier.align(Alignment.TopCenter))
        FadeEffectBottom(Modifier.align(Alignment.BottomCenter))
    }
}

@Preview
@Composable
private fun MoreAppsContentPreview() {
    AppTheme {
        MoreAppsContent(
            state = MoreAppsState(AppModel.all()),
            callbacks = MoreAppsCallbacks()
        )
    }
}
