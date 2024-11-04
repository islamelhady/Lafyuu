package com.elhady.lafyuu.feature.checkout.data.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.checkout.data.mapper.toDomain
import com.elhady.lafyuu.feature.checkout.data.remote.CheckoutApi
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val checkoutApi: CheckoutApi,
    private val safeApiCaller: SafeApiCaller
) : CheckoutRepository {

    override suspend fun checkout(
        shippingAddressId: String,
        paymentMethod: String,
        couponCode: String?
    ): AppResult<CheckoutResult> {
        return safeApiCaller.call {
            checkoutApi.checkout(
                OrderCheckoutRequestDto(
                    shippingAddressId,
                    paymentMethod,
                    couponCode
                )
            ).toDomain()
        }
    }
}
