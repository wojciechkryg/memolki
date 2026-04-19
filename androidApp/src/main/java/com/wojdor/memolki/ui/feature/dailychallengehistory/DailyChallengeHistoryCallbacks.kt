package com.wojdor.memolki.ui.feature.dailychallengehistory

import com.wojdor.memolki.domain.model.DailyChallengeModel

data class DailyChallengeHistoryCallbacks(
    val onShareClick: (DailyChallengeModel) -> Unit = {}
)
