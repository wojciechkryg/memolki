package com.wojdor.memolki.data.mapper

object CardFlipCountsMapper {

    fun serialize(cardFlipCounts: List<List<Int>>): String =
        cardFlipCounts.joinToString(";") { row -> row.joinToString(",") }

    fun deserialize(value: String): List<List<Int>> {
        if (value.isEmpty()) return emptyList()
        return runCatching {
            value.split(";").map { row ->
                row.split(",").map { token -> token.toInt() }
            }
        }.getOrElse { emptyList() }
    }
}
