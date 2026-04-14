package com.wojdor.memolki.ui.feature.dailychallengehistory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.ui.component.CompareButton
import com.wojdor.memolki.ui.component.TimeDisplay
import com.wojdor.memolki.ui.feature.game.component.CardBorder
import com.wojdor.memolki.ui.theme.AppTheme
import com.wojdor.memolki.ui.theme.CardShape
import com.wojdor.memolki.ui.theme.spacingL
import com.wojdor.memolki.ui.theme.spacingS
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun DailyChallengeHistoryItem(
    challenge: DailyChallengeModel,
    isToday: Boolean,
    onShareClick: () -> Unit = {}
) {
    CardBorder(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingL),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(challenge.epochDay),
                    style = MaterialTheme.typography.headlineMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(challenge.starCount) {
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(spacingS))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeDisplay(
                    timeMillis = challenge.timeMillis,
                    textStyle = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.daily_challenge_mistakes,
                        challenge.mistakeCount,
                        challenge.mistakeCount
                    ),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (isToday) {
                Spacer(modifier = Modifier.size(spacingS))
                CompareButton(
                    onClick = onShareClick,
                    contentPadding = PaddingValues(horizontal = spacingL, vertical = spacingS),
                    textStyle = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}

private fun formatDate(epochDay: Long): String {
    val date = LocalDate.ofEpochDay(epochDay)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
    return date.format(formatter)
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeHistoryItemPreview() {
    AppTheme {
        DailyChallengeHistoryItem(
            challenge = DailyChallengeModel(
                epochDay = LocalDate.of(2026, 4, 11).toEpochDay(),
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 83456L,
                cardFlipCounts = listOf(
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2)
                )
            ),
            isToday = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeHistoryItemTodayPreview() {
    AppTheme {
        DailyChallengeHistoryItem(
            challenge = DailyChallengeModel(
                epochDay = LocalDate.of(2026, 4, 11).toEpochDay(),
                mistakeCount = 0,
                starCount = 3,
                timeMillis = 83456L,
                cardFlipCounts = listOf(
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2),
                    listOf(2, 2, 2, 2)
                )
            ),
            isToday = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyChallengeHistoryItemOneStarPreview() {
    AppTheme {
        DailyChallengeHistoryItem(
            challenge = DailyChallengeModel(
                epochDay = LocalDate.of(2026, 4, 9).toEpochDay(),
                mistakeCount = 7,
                starCount = 1,
                timeMillis = 245123L,
                cardFlipCounts = listOf(
                    listOf(4, 3, 5, 2),
                    listOf(3, 6, 2, 4),
                    listOf(2, 3, 4, 5)
                )
            ),
            isToday = false
        )
    }
}
