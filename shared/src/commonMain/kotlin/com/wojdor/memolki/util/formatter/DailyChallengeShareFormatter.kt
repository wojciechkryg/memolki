package com.wojdor.memolki.util.formatter

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.daily_challenge_mistakes
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.provider.TimeProvider
import com.wojdor.memolki.util.resource.StringProvider
import kotlinx.datetime.LocalDate

class DailyChallengeShareFormatter(
    private val packageNameProvider: PackageNameProvider,
    private val timeProvider: TimeProvider,
    private val timeFormatter: TimeFormatter,
    private val stringProvider: StringProvider
) {

    suspend fun format(result: DailyChallengeModel): String {
        val packageName = packageNameProvider.providePackageName()
        val appName = stringProvider.getString(AppModel.all().first { it.appId == packageName }.appNameRes)
        val mistakeText = stringProvider.getPluralString(
            Res.plurals.daily_challenge_mistakes,
            result.mistakeCount,
            result.mistakeCount
        )
        return formatText(
            result = result,
            appName = appName,
            mistakeText = mistakeText,
            packageName = packageName
        )
    }

    fun formatText(
        result: DailyChallengeModel,
        appName: String,
        mistakeText: String,
        packageName: String
    ): String {
        val grid = result.cardFlipCounts
            .map { row -> row.map { it <= MAX_PERFECT_FLIPS } }
        val date = formatDate(timeProvider.localDateFromEpochDay(result.epochDay))
        val stars = starsEmoji(result.starCount)
        val time = timeFormatter.format(result.timeMillis)
        val timeText = "${time.main.replace(":", RATIO)}${time.millis.replace(".", ONE_DOT_LEADER)}"
        val gridText = buildGrid(grid)
        return buildString {
            appendLine("🃏 $appName • $date")
            appendLine("⏱️ $timeText")
            appendLine("$stars $mistakeText")
            appendLine(gridText)
            append("https://play.google.com/store/apps/details?id=$packageName")
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
