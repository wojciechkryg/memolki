package com.wojdor.memolki.ui.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.net.toUri
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.ClickIndicatorOverlay
import com.wojdor.memolki.ui.component.ForceLtr
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.LocalWindowSize
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.notification.NotificationScheduler
import com.wojdor.memolki.util.notification.DeepLinkBuilder
import com.wojdor.memolki.util.notification.NotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import com.wojdor.memolki.util.update.InAppUpdate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: AppViewModel

    @Inject
    lateinit var backgroundMusicPlayer: BackgroundMusicPlayer

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    @Inject
    lateinit var inAppUpdate: InAppUpdate

    private val newIntentState = mutableStateOf<Intent?>(null)

    companion object {
        private const val EXTRA_SHORTCUT_ID = "shortcut_id"
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAppCreate()
        viewModel.onAppOpen(
            notificationType = intent?.getStringExtra(EXTRA_NOTIFICATION_TYPE),
            shortcutId = intent?.getStringExtra(EXTRA_SHORTCUT_ID)
        )
        resolveDeepLinkIntent(intent)?.let { newIntentState.value = it }
        lifecycle.addObserver(backgroundMusicPlayer)
        lifecycle.addObserver(notificationScheduler)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        inAppUpdate.checkUpdate(this)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            CompositionLocalProvider(LocalWindowSize provides windowSizeClass) {
                AppTheme {
                    val appContent = @Composable {
                        ClickIndicatorOverlay {
                            Scaffold(
                                containerColor = colorResource(R.color.primary),
                                content = { innerPadding ->
                                    Box(modifier = Modifier.padding(innerPadding)) {
                                        AppNavigation(
                                            onNewIntent = newIntentState.value,
                                            onIntentHandled = { newIntentState.value = null }
                                        )
                                    }
                                }
                            )
                        }
                    }
                    if (RECORDING_MODE) ForceLtr { appContent() } else appContent()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        newIntentState.value = resolveDeepLinkIntent(intent) ?: intent
        viewModel.onAppOpen(
            notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE),
            shortcutId = intent.getStringExtra(EXTRA_SHORTCUT_ID)
        )
    }

    private fun resolveDeepLinkIntent(intent: Intent?): Intent? {
        if (intent == null) return null
        if (intent.action == Intent.ACTION_VIEW && intent.data != null) return intent
        val screen = intent.getStringExtra(DeepLinkBuilder.EXTRA_SCREEN) ?: return null
        val level = intent.getStringExtra(DeepLinkBuilder.EXTRA_LEVEL)
        val deepLinkUri = DeepLinkBuilder.buildUri(screen, level) ?: return null
        return Intent(Intent.ACTION_VIEW, deepLinkUri.toUri(), this, AppActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.resumeUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.cleanup()
    }
}
