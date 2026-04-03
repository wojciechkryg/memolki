package com.wojdor.memolki.data.local.database.dailychallenge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailyChallengeDao {

    @Query("SELECT * FROM daily_challenges WHERE epochDay = :epochDay")
    suspend fun getResult(epochDay: Long): DailyChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: DailyChallengeEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM daily_challenges WHERE epochDay = :epochDay)")
    suspend fun hasPlayed(epochDay: Long): Boolean

    @Query("SELECT MAX(epochDay) FROM daily_challenges")
    suspend fun getLastPlayedEpochDay(): Long?
}
