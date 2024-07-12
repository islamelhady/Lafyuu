package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String, quantity: Int): AppResult<Unit> {
        if (quantity <= 0) {
            return AppResult.Error("Quantity must be greater than zero")
        }
        return repository.updateItemQuantity(itemId, quantity)
    }
}
