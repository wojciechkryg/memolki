package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.mapper.toEntity
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.domain.model.DailyChallengeModel

class DailyChallengeRepository(
    private val dailyChallengeDao: DailyChallengeDao
) {

    suspend fun hasPlayed(epochDay: Long): Boolean =
        dailyChallengeDao.hasPlayed(epochDay)

    suspend fun getResult(epochDay: Long): DailyChallengeModel? =
        dailyChallengeDao.getResult(epochDay)?.toModel()

    suspend fun saveResult(epochDay: Long, model: DailyChallengeModel) =
        dailyChallengeDao.insertResult(model.toEntity(epochDay))

    suspend fun getLastPlayedEpochDay(): Long? =
        dailyChallengeDao.getLastPlayedEpochDay()

    suspend fun getAll(): List<DailyChallengeModel> =
        dailyChallengeDao.getAll().map { it.toModel() }

    suspend fun hasAnyCompleted(): Boolean =
        dailyChallengeDao.hasAnyCompleted()
}
