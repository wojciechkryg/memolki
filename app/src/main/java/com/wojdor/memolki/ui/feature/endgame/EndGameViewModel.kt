package com.wojdor.memolki.ui.feature.endgame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wojdor.memolki.domain.model.LevelModel
import com.wojdor.memolki.domain.usecase.RewardCoinsForLevelUseCase
import com.wojdor.memolki.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rewardCoinsForLevelUseCase: RewardCoinsForLevelUseCase
) : MviViewModel<EndGameIntent, EndGameState>(
    savedStateHandle,
    EndGameState()
) {

    override fun onIntent(intent: EndGameIntent) {
        when (intent) {
            is EndGameIntent.OnEndGameShow -> onEndGameShow(level = intent.levelModel)
        }
    }

    private fun onEndGameShow(level: LevelModel) {
        rewardCoinsForLevelUseCase(level).onEach {
            it.onSuccess { rewardedCoins ->
                sendState { copy(level = level, rewardedCoins = rewardedCoins) }
            }
        }.launchIn(viewModelScope)
    }
}
