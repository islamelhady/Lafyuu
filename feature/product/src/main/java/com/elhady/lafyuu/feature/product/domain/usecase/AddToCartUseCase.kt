package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int): AppResult<String?> =
        repository.addToCart(productId, quantity)
}
