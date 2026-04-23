package com.wojdor.memolki.ui.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.ClickIndicatorOverlay
import com.wojdor.memolki.ui.component.ForceLtr
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.LocalScreenHeight
import com.wojdor.memolki.ui.theme.LocalScreenWidth
import com.wojdor.memolki.ui.theme.AppColors
import com.wojdor.memolki.util.media.AndroidBackgroundMusicPlayer
import com.wojdor.memolki.util.notification.DeepLink
import com.wojdor.memolki.util.notification.DeepLinkBuilder
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler
import com.wojdor.memolki.util.notification.AndroidNotificationScheduler.Companion.EXTRA_NOTIFICATION_TYPE
import com.wojdor.memolki.util.provider.RecordingModeProvider.RECORDING_MODE
import com.wojdor.memolki.util.update.InAppUpdate
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModel()
    private val backgroundMusicPlayer: AndroidBackgroundMusicPlayer by inject()
    private val inAppUpdate: InAppUpdate by inject()
    private val notificationScheduler: AndroidNotificationScheduler by inject()

    private val deepLinkState = mutableStateOf<DeepLink?>(null)

    companion object {
        private const val EXTRA_SHORTCUT_ID = "shortcut_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAppCreate()
        viewModel.onAppOpen(
            notificationType = intent?.getStringExtra(EXTRA_NOTIFICATION_TYPE),
            shortcutId = intent?.getStringExtra(EXTRA_SHORTCUT_ID)
        )
        deepLinkState.value = resolveDeepLink(intent)
        lifecycle.addObserver(backgroundMusicPlayer)
        lifecycle.addObserver(notificationScheduler)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        if (RECORDING_MODE) {
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        inAppUpdate.checkUpdate(this)
        setContent {
            val configuration = LocalConfiguration.current
            CompositionLocalProvider(
                LocalScreenWidth provides configuration.screenWidthDp.dp,
                LocalScreenHeight provides configuration.screenHeightDp.dp
            ) {
                AppTheme {
                    val appContent = @Composable {
                        ClickIndicatorOverlay {
                            Scaffold(
                                containerColor = AppColors.Primary,
                                content = { innerPadding ->
                                    Box(modifier = Modifier.padding(innerPadding)) {
                                        AppNavigation(
                                            deepLink = deepLinkState.value,
                                            onDeepLinkHandled = { deepLinkState.value = null },
                                            hasPlayedTodayDailyChallenge = { viewModel.hasPlayedTodayDailyChallenge() }
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
        deepLinkState.value = resolveDeepLink(intent)
        viewModel.onAppOpen(
            notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE),
            shortcutId = intent.getStringExtra(EXTRA_SHORTCUT_ID)
        )
    }

    private fun resolveDeepLink(intent: Intent?): DeepLink? {
        if (intent == null) return null
        val data = if (intent.action == Intent.ACTION_VIEW) intent.data else null
        if (data != null) {
            val host = data.host ?: return null
            return DeepLink(host = host, pathSegments = data.pathSegments.orEmpty())
        }
        val screen = intent.getStringExtra(DeepLinkBuilder.EXTRA_SCREEN) ?: return null
        val board = intent.getStringExtra(DeepLinkBuilder.EXTRA_BOARD)
        val uriString = DeepLinkBuilder.buildUri(screen, board) ?: return null
        val uri = uriString.toUri()
        val host = uri.host ?: return null
        return DeepLink(host = host, pathSegments = uri.pathSegments.orEmpty())
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.resumeUpdate(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.cleanup()
    }
}
