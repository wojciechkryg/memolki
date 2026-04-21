package com.wojdor.memolki.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "memolki_database"

fun databaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> =
    Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = DATABASE_NAME
    )
