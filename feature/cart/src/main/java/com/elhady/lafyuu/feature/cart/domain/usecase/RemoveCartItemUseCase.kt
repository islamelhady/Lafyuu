package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class RemoveCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(itemId: String): AppResult<Unit> {
        if (itemId.isBlank()) {
            return AppResult.Error("Invalid item ID")
        }
        return repository.removeItem(itemId)
    }
}
