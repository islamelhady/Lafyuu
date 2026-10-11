package com.elhady.lafyuu.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class LafyuuDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
