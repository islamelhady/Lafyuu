package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class DecreaseCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String): AppResult<Unit> {
        if (itemId.isBlank()) {
            return AppResult.Error("Invalid item ID")
        }
        val cartResult = repository.getCart()
        if (cartResult is AppResult.Success) {
            val item = cartResult.data.items.find { it.itemId == itemId }
            if (item != null) {
                if (item.quantity <= 1) {
                    return repository.removeItem(itemId)
                } else {
                    return repository.decrementItem(itemId, 1)
                }
            } else {
                return AppResult.Error("Item not found in cart")
            }
        }
        return (cartResult as? AppResult.Error) ?: AppResult.Error("Failed to retrieve cart")
    }
}
