package com.elhady.lafyuu.feature.favorites.data.repository

import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.feature.favorites.data.mapper.toDomain
import com.elhady.lafyuu.feature.favorites.domain.model.FavoriteProduct
import com.elhady.lafyuu.feature.favorites.domain.repository.FavoriteProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteProductsRepositoryImpl @Inject constructor(
    private val favorites: FavoritesRepository
) : FavoriteProductsRepository {

    override fun observeFavorites(): Flow<List<FavoriteProduct>> =
        favorites.observeFavorites().map { items -> items.map { it.toDomain() } }

    override suspend fun removeFavorite(productId: String) {
        favorites.removeFavorite(productId)
    }
}