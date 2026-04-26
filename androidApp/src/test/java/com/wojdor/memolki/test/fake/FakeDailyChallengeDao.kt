package com.wojdor.memolki.test.fake

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity

class FakeDailyChallengeDao : DailyChallengeDao {
    private val entities = mutableMapOf<Long, DailyChallengeEntity>()

    override suspend fun getResult(epochDay: Long): DailyChallengeEntity? = entities[epochDay]

    override suspend fun insertResult(result: DailyChallengeEntity) {
        entities[result.epochDay] = result
    }

    override suspend fun hasPlayed(epochDay: Long): Boolean = epochDay in entities

    override suspend fun getLastPlayedEpochDay(): Long? = entities.keys.maxOrNull()

    override suspend fun getAll(): List<DailyChallengeEntity> =
        entities.values.filter { it.starCount > 0 }.sortedByDescending { it.epochDay }

    override suspend fun hasAnyCompleted(): Boolean = entities.values.any { it.starCount > 0 }
}
