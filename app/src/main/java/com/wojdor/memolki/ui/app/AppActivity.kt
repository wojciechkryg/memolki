package com.wojdor.memolki.ui.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.wojdor.memolki.R
import com.wojdor.memolki.games.GooglePlayGames
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.util.extension.logE
import com.wojdor.memolki.util.media.BackgroundMusicPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: AppViewModel

    @Inject
    lateinit var backgroundMusicPlayer: BackgroundMusicPlayer

    @Inject
    lateinit var googlePlayGames: GooglePlayGames

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.unlockAllNewCardPairsIfPurchased()
        lifecycle.addObserver(backgroundMusicPlayer)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        signInToPlayGames()
        setContent {
            AppTheme {
                Scaffold(
                    containerColor = colorResource(R.color.primary),
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigation()
                        }
                    }
                )
            }
        }
    }

    private fun signInToPlayGames() {
        lifecycleScope.launch {
            try {
                googlePlayGames.signIn(this@AppActivity)
            } catch (error: Exception) {
                logE("Cannot sign in to Play Games", error)
            }
        }
    }
}
