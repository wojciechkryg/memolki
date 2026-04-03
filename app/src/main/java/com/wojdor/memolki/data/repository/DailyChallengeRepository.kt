package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.mapper.toEntity
import com.wojdor.memolki.data.mapper.toModel
import com.wojdor.memolki.domain.model.DailyChallengeModel
import javax.inject.Inject

class DailyChallengeRepository @Inject constructor(
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
}
