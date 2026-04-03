package com.wojdor.memolki.data.mapper

object CardFlipCountsMapper {

    fun serialize(cardFlipCounts: List<List<Int>>): String =
        cardFlipCounts.joinToString(";") { row -> row.joinToString(",") }

    fun deserialize(value: String): List<List<Int>> =
        value.takeIf { it.isNotEmpty() }
            ?.split(";")
            ?.map { row -> row.split(",").map { it.toInt() } }
            ?: emptyList()
}
