package com.wojdor.memolki.util.formatter

import android.content.Context
import com.wojdor.memolki.R
import com.wojdor.memolki.domain.model.BoardModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CasualShareFormatter @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun format(board: BoardModel, level: Long): String {
        val appName = context.getString(R.string.app_name)
        val boardSize = "${board.columns}x${board.rows}"
        val storeLink = "https://play.google.com/store/apps/details?id=${context.packageName}"
        return buildString {
            appendLine("🃏 $appName")
            appendLine("🧩 $boardSize • ${context.getString(R.string.level_count, level)}")
            appendLine(context.getString(R.string.share_casual))
            append(storeLink)
        }
    }
}
