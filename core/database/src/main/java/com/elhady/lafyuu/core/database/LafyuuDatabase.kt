package com.elhady.lafyuu.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 2, exportSchema = false)
abstract class LafyuuDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}

/**
 * v1 stored product ids only, which can't be displayed without a network call,
 * so old rows are dropped and the table is recreated with the product snapshot.
 */
internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `favorites`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `favorites` (" +
                    "`productId` TEXT NOT NULL, " +
                    "`name` TEXT NOT NULL, " +
                    "`imageUrl` TEXT, " +
                    "`price` REAL NOT NULL, " +
                    "`originalPrice` REAL, " +
                    "`discountPercentage` INTEGER NOT NULL, " +
                    "`rating` INTEGER NOT NULL, " +
                    "`addedAt` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`productId`))"
        )
    }
}
