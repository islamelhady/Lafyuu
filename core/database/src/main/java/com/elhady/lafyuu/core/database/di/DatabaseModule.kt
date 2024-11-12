package com.elhady.lafyuu.core.database.di

import android.content.Context
import androidx.room.Room
import com.elhady.lafyuu.core.database.LafyuuDatabase
import com.elhady.lafyuu.core.database.MIGRATION_1_2
import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.core.database.repository.FavoritesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLafyuuDatabase(
        @ApplicationContext context: Context
    ): LafyuuDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LafyuuDatabase::class.java,
            "lafyuu_database"
        ).addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(
        database: LafyuuDatabase
    ): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        favoriteDao: FavoriteDao
    ): FavoritesRepository {
        return FavoritesRepositoryImpl(favoriteDao)
    }
}
