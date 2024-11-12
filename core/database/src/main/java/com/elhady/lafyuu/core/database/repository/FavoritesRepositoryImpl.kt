package com.elhady.lafyuu.core.database.repository

import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.entity.FavoriteEntity
import com.elhady.lafyuu.core.database.model.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Favorite>> =
        favoriteDao.observeFavorites().map { entities -> entities.map { it.toFavorite() } }

    override fun isFavorite(productId: String): Flow<Boolean> = favoriteDao.isFavorite(productId)

    override suspend fun addFavorite(item: Favorite) {
        if (item.productId.isBlank()) return
        favoriteDao.insertFavorite(item.toEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun removeFavorite(productId: String) {
        if (productId.isNotBlank()) favoriteDao.deleteFavorite(productId)
    }

    override suspend fun clearFavorites() {
        favoriteDao.clearFavorites()
    }
}

private fun FavoriteEntity.toFavorite() = Favorite(
    productId = productId,
    name = name,
    imageUrl = imageUrl,
    price = price,
    originalPrice = originalPrice,
    discountPercentage = discountPercentage,
    rating = rating
)

private fun Favorite.toEntity(addedAt: Long) = FavoriteEntity(
    productId = productId,
    name = name,
    imageUrl = imageUrl,
    price = price,
    originalPrice = originalPrice,
    discountPercentage = discountPercentage,
    rating = rating,
    addedAt = addedAt
)