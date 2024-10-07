package com.elhady.lafyuu.feature.checkout.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.checkout.data.mapper.toDomain
import com.elhady.lafyuu.feature.checkout.data.remote.CheckoutApi
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val checkoutApi: CheckoutApi,
    private val apiErrorParser: ApiErrorParser
) : CheckoutRepository {

    override suspend fun checkout(
        shippingAddressId: String,
        paymentMethod: String,
        couponCode: String?
    ): AppResult<CheckoutResult> {
        return try {
            val response = checkoutApi.checkout(
                OrderCheckoutRequestDto(
                    shippingAddressId = shippingAddressId,
                    paymentMethod = paymentMethod,
                    couponCode = couponCode
                )
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Checkout response is empty")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
