package com.wojdor.memolki.util.formatter

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.util.provider.TimeProvider
import kotlinx.datetime.LocalDate

class DailyChallengeShareFormatter(
    private val context: Context,
    private val timeProvider: TimeProvider,
    private val timeFormatter: TimeFormatter
) {

    fun format(result: DailyChallengeModel): String {
        val grid = result.cardFlipCounts
            .map { row -> row.map { it <= MAX_PERFECT_FLIPS } }
        return formatWithGrid(result, grid)
    }

    private fun formatWithGrid(
        result: DailyChallengeModel,
        grid: List<List<Boolean>>
    ): String {
        val appName = context.getString(R.string.app_name)
        val date = formatDate(timeProvider.localDateFromEpochDay(result.epochDay))
        val stars = starsEmoji(result.starCount)
        val mistakeText = context.resources.getQuantityString(
            R.plurals.daily_challenge_mistakes,
            result.mistakeCount,
            result.mistakeCount
        )
        val time = timeFormatter.format(result.timeMillis)
        val timeText = "${time.main.replace(":", RATIO)}${time.millis.replace(".", ONE_DOT_LEADER)}"
        val gridText = buildGrid(grid)
        val storeLink = "https://play.google.com/store/apps/details?id=${context.packageName}"
        return buildString {
            appendLine("🃏 $appName • $date")
            appendLine("⏱️ $timeText")
            appendLine("$stars $mistakeText")
            appendLine(gridText)
            append(storeLink)
        }
    }

    private fun formatDate(date: LocalDate): String {
        val day = date.day.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        return "$day$DIVISION_SLASH$month$DIVISION_SLASH${date.year}"
    }

    private fun starsEmoji(starCount: Int): String = when (starCount) {
        3 -> "⭐⭐⭐"
        2 -> "⭐⭐"
        else -> "⭐"
    }

    private fun buildGrid(grid: List<List<Boolean>>): String {
        return grid.joinToString("\n") { row ->
            row.joinToString("") { isPerfect ->
                if (isPerfect) "🟩" else "🟥"
            }
        }
    }

    companion object {
        private const val MAX_PERFECT_FLIPS = 2
        // Unicode look-alikes to prevent auto-linking in messaging apps
        private const val DIVISION_SLASH = "\u2215"
        private const val RATIO = "\u2236"
        private const val ONE_DOT_LEADER = "\u2024"
    }
}
