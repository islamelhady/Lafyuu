package com.elhady.lafyuu

import com.elhady.lafyuu.core.database.model.FavoriteItem
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.feature.cart.domain.model.CartItem
import com.elhady.lafyuu.feature.cart.domain.repository.CartFavoritesGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartFavoritesGatewayImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : CartFavoritesGateway {

    override fun observeFavoriteProductIds(): Flow<Set<String>> =
        favoritesRepository.observeFavoriteIds().map { it.toSet() }

    override suspend fun addFavorite(item: CartItem) {
        favoritesRepository.addFavorite(
            FavoriteItem(
                productId = item.productId,
                name = item.productName,
                imageUrl = item.productCoverUrl.takeIf { it.isNotBlank() },
                price = item.finalPricePerUnit,
                originalPrice = item.basePricePerUnit.takeIf { it > item.finalPricePerUnit },
                discountPercentage = item.discountPercentage.toInt(),
                rating = 0 // the cart API doesn't return a rating
            )
        )
    }

    override suspend fun removeFavorite(productId: String) {
        favoritesRepository.removeFavorite(productId)
    }
}