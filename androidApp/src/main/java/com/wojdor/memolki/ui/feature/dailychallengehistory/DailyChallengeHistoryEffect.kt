package com.wojdor.memolki.ui.feature.dailychallengehistory

import com.wojdor.memolki.ui.base.UiEffect

sealed class DailyChallengeHistoryEffect : UiEffect {
    data class ShareDailyChallenge(val text: String) : DailyChallengeHistoryEffect()
}
