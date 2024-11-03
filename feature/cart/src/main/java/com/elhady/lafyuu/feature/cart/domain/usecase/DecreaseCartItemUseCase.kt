package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class DecreaseCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String): AppResult<Unit> {
        val cart = when (val result = repository.currentCart()) {
            is AppResult.Success -> result.data
            is AppResult.Failure -> return result
        }
        val item = cart.items.find { it.itemId == itemId }
            ?: return AppResult.Failure(AppError.NotFound)

        return if (item.quantity <= 1) repository.removeItem(itemId)
        else repository.decrementItem(itemId, 1)
    }
}
