package com.wojdor.memolki.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeDao
import com.wojdor.memolki.data.local.database.dailychallenge.DailyChallengeEntity

@Database(
    entities = [DailyChallengeEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyChallengeDao(): DailyChallengeDao
}

private const val DATABASE_VERSION = 1
