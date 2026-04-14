package com.wojdor.memolki.ui.feature.dailychallengehistory

import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.ui.base.UiIntent

sealed class DailyChallengeHistoryIntent : UiIntent {
    data class OnShareClick(val dailyChallenge: DailyChallengeModel) : DailyChallengeHistoryIntent()
}
