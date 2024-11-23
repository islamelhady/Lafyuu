package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.model.PaymentMethod
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(
        shippingAddressId: String,
        paymentMethod: PaymentMethod,
        couponCode: String?
    ): AppResult<CheckoutResult> {
        if (shippingAddressId.isBlank()) {
            return AppResult.Failure(AppError.Validation("Shipping address is missing"))
        }
        return repository.checkout(
            shippingAddressId = shippingAddressId,
            paymentMethod = paymentMethod,
            couponCode = couponCode?.trim()?.takeIf { it.isNotEmpty() }
        )
    }
}