package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class ApplyCouponUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(couponCode: String): AppResult<Cart> =
        repository.applyCoupon(couponCode.trim())
}
