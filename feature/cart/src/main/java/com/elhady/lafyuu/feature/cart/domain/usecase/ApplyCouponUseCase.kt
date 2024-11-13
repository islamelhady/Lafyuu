package com.elhady.lafyuu.feature.cart.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class ApplyCouponUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(couponCode: String): AppResult<Cart> {
        val code = couponCode.trim()
        if (code.isEmpty()) {
            return AppResult.Failure(AppError.Validation("Coupon code is empty"))
        }
        return repository.applyCoupon(couponCode = code)
    }
}