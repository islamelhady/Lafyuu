package com.elhady.lafyuu

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.map
import com.elhady.lafyuu.core.database.model.FavoriteItem
import com.elhady.lafyuu.core.database.repository.FavoritesRepository
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import com.elhady.lafyuu.feature.cart.domain.usecase.RemoveCartItemUseCase
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.repository.ProductCartGateway
import com.elhady.lafyuu.feature.product.domain.repository.ProductFavoriteGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductCartGatewayImpl @Inject constructor(
    private val cartRepository: CartRepository,
    private val removeCartItemUseCase: RemoveCartItemUseCase
) : ProductCartGateway {

    override fun observeCartItemId(productId: String): Flow<String?> {
        return cartRepository.observeCart()
            .map { cart -> cart?.items?.firstOrNull { it.productId == productId }?.itemId }
            .distinctUntilChanged()
    }

    override suspend fun removeCartItem(cartItemId: String): AppResult<Unit> {
        return removeCartItemUseCase(cartItemId).map { }
    }

    override suspend fun refresh() {
        cartRepository.getCart()
    }
}

class ProductFavoriteGatewayImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ProductFavoriteGateway {

    override fun isFavorite(productId: String): Flow<Boolean> =
        favoritesRepository.isFavorite(productId)

    override suspend fun addFavorite(product: ProductDetails) {
        favoritesRepository.addFavorite(
            FavoriteItem(
                productId = product.id,
                name = product.name,
                imageUrl = product.pictureUrls.firstOrNull(),
                price = product.finalPrice,
                originalPrice = product.basePrice.takeIf { it > product.finalPrice },
                discountPercentage = product.discountPercentage,
                rating = product.rating
            )
        )
    }

    override suspend fun removeFavorite(productId: String) {
        favoritesRepository.removeFavorite(productId)
    }
}