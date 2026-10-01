package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String, targetQuantity: Int): AppResult<Unit> {
        if (itemId.isBlank()) {
            return AppResult.Error("Invalid item ID")
        }
        if (targetQuantity <= 0) {
            return repository.removeItem(itemId)
        }
        val cartResult = repository.getCart()
        if (cartResult is AppResult.Success) {
            val item = cartResult.data.items.find { it.itemId == itemId }
            if (item != null) {
                if (targetQuantity > item.productStock) {
                    return AppResult.Error("Cannot exceed available stock (${item.productStock})")
                }
            }
        }
        return repository.updateItemQuantity(itemId, targetQuantity)
    }
}
