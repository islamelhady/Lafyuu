package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): AppResult<Cart> {
        return repository.getCart()
    }
}
