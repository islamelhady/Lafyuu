package com.elhady.lafyuu.feature.checkout.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val repository: CheckoutRepository
) {
    suspend operator fun invoke(
        shippingAddressId: String,
        paymentMethod: String,
        couponCode: String?
    ): AppResult<CheckoutResult> {
        return repository.checkout(shippingAddressId, paymentMethod, couponCode)
    }
}
