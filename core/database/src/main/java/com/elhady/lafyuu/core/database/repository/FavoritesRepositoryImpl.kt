package com.elhady.lafyuu.core.database.repository

import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesRepository {

    override fun observeFavoriteIds(): Flow<List<String>> = favoriteDao.observeFavoriteIds()

    override fun isFavorite(productId: String): Flow<Boolean> = favoriteDao.isFavorite(productId)

    override suspend fun addFavorite(productId: String) {
        if (productId.isNotBlank()) {
            favoriteDao.insertFavorite(FavoriteEntity(productId))
        }
    }

    override suspend fun removeFavorite(productId: String) {
        if (productId.isNotBlank()) {
            favoriteDao.deleteFavorite(productId)
        }
    }

    override suspend fun clearFavorites() {
        favoriteDao.clearFavorites()
    }
}
