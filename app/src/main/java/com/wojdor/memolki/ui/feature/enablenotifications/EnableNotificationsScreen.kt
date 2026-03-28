package com.wojdor.memolki.ui.feature.enablenotifications

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.app.navigateToCollection
import com.wojdor.memolki.ui.app.navigateToGameFromEndGame
import com.wojdor.memolki.ui.app.navigateToMenu
import com.wojdor.memolki.ui.app.navigateToShop
import com.wojdor.memolki.ui.base.CollectUiEffects
import com.wojdor.memolki.ui.component.BaseMenuItem
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingM
import com.wojdor.memolki.ui.theme.spacingXL
import com.wojdor.memolki.util.throttleClick

@Composable
fun EnableNotificationsScreen(
    viewModel: EnableNotificationsViewModel = hiltViewModel(),
    navController: NavController
) {
    BackHandler { }
    val state by viewModel.uiState.collectAsState()
    HandleEffect(viewModel, navController)
    HandleState(viewModel, state)
}

@Composable
private fun HandleEffect(
    viewModel: EnableNotificationsViewModel,
    navController: NavController
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.sendIntent(EnableNotificationsIntent.OnPermissionResult)
    }
    CollectUiEffects(viewModel) { effect ->
        when (effect) {
            EnableNotificationsEffect.RequestNotificationPermission ->
                requestNotificationPermission(permissionLauncher, viewModel)

            EnableNotificationsEffect.NavigateToGame -> navController.navigateToGameFromEndGame()
            EnableNotificationsEffect.NavigateToMenu -> navController.navigateToMenu()
            EnableNotificationsEffect.NavigateToCollection -> navController.navigateToCollection()
            EnableNotificationsEffect.NavigateToShop -> navController.navigateToShop()
        }
    }
}

private fun requestNotificationPermission(
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: EnableNotificationsViewModel
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        viewModel.sendIntent(EnableNotificationsIntent.OnPermissionResult)
    }
}

@Composable
private fun HandleState(
    viewModel: EnableNotificationsViewModel,
    state: EnableNotificationsState
) {
    val callbacks = EnableNotificationsCallbacks(
        onEnableClick = { viewModel.sendIntent(EnableNotificationsIntent.OnEnableClick) },
        onLaterClick = { viewModel.sendIntent(EnableNotificationsIntent.OnLaterClick) }
    )
    EnableNotificationsScreen(state, callbacks)
}

@Composable
private fun EnableNotificationsScreen(
    state: EnableNotificationsState,
    callbacks: EnableNotificationsCallbacks = EnableNotificationsCallbacks()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacingXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_coins_sack),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(spacingXL))
        Text(
            text = stringResource(R.string.enable_notifications_title),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(spacingM))
        Text(
            text = stringResource(R.string.enable_notifications_body),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(spacingXL))
        BaseMenuItem(
            textId = R.string.enable_notifications_enable,
            onClick = callbacks.onEnableClick
        )
        var isLaterVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(LATER_BUTTON_DELAY)
            isLaterVisible = true
        }
        val laterAlpha by animateFloatAsState(
            targetValue = if (isLaterVisible) 1f else 0f,
            label = "later button alpha animation"
        )
        TextButton(
            onClick = throttleClick(onClick = callbacks.onLaterClick),
            enabled = isLaterVisible,
            modifier = Modifier
                .padding(top = spacingL)
                .alpha(laterAlpha)
        ) {
            Text(
                text = stringResource(R.string.enable_notifications_later).lowercase(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
    }
}

private const val LATER_BUTTON_DELAY = 1500L

@Composable
@Preview(showBackground = true)
private fun EnableNotificationsScreenPreview() {
    AppTheme {
        EnableNotificationsScreen(
            state = EnableNotificationsState()
        )
    }
}
