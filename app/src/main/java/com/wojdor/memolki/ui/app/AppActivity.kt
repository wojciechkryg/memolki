package com.wojdor.memolki.ui.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wojdor.memolki.R
import com.wojdor.memolki.ui.component.ClickIndicatorOverlay
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.LocalWindowSize
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import com.wojdor.memolki.util.notification.NotificationScheduler
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.unlockAllNewCardPairsIfPurchased()
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
                    ClickIndicatorOverlay {
                        Scaffold(
                            containerColor = colorResource(R.color.primary),
                            content = { innerPadding ->
                                Box(modifier = Modifier.padding(innerPadding)) {
                                    AppNavigation(onNewIntent = newIntentState.value)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        newIntentState.value = intent
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
