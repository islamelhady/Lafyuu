package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String, targetQuantity: Int): AppResult<Unit> {
        if (targetQuantity <= 0) return repository.removeItem(itemId)

        val cart = when (val result = repository.currentCart()) {
            is AppResult.Success -> result.data
            is AppResult.Failure -> return result
        }
        val item = cart.items.find { it.itemId == itemId }
            ?: return AppResult.Failure(AppError.NotFound)

        if (targetQuantity > item.productStock) {
            return AppResult.Failure(AppError.OutOfStock(item.productStock))
        }
        return repository.updateItemQuantity(itemId, targetQuantity)
    }
}
