package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int): AppResult<Unit> {
        if (productId.isBlank()) {
            return AppResult.Error("Invalid product ID")
        }
        if (quantity <= 0) {
            return AppResult.Error("Quantity must be greater than zero")
        }
        return repository.addToCart(productId, quantity)
    }
}
