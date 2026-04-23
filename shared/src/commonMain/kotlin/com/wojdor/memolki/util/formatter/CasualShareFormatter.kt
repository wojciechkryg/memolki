package com.wojdor.memolki.util.formatter

import com.wojdor.memolki.domain.model.AppModel
import com.wojdor.memolki.domain.model.BoardModel
import com.wojdor.memolki.shared.resources.Res
import com.wojdor.memolki.shared.resources.level_count
import com.wojdor.memolki.shared.resources.share_casual
import com.wojdor.memolki.util.provider.PackageNameProvider
import com.wojdor.memolki.util.resource.StringProvider

class CasualShareFormatter(
    private val packageNameProvider: PackageNameProvider,
    private val stringProvider: StringProvider
) {

    suspend fun format(board: BoardModel, level: Long): String {
        val packageName = packageNameProvider.providePackageName()
        val appName = stringProvider.getString(AppModel.all().first { it.appId == packageName }.appNameRes)
        val levelText = stringProvider.getString(Res.string.level_count, level)
        val shareMessage = stringProvider.getString(Res.string.share_casual)
        return formatText(
            appName = appName,
            boardSize = "${board.columns}x${board.rows}",
            levelText = levelText,
            shareMessage = shareMessage,
            packageName = packageName
        )
    }

    fun formatText(
        appName: String,
        boardSize: String,
        levelText: String,
        shareMessage: String,
        packageName: String
    ): String = buildString {
        appendLine("🃏 $appName")
        appendLine("🧩 $boardSize • $levelText")
        appendLine(shareMessage)
        append("https://play.google.com/store/apps/details?id=$packageName")
    }
}
