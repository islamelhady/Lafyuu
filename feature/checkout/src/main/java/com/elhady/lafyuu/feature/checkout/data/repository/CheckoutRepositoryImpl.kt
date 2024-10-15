package com.elhady.lafyuu.feature.checkout.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.error.toErrorMessage
import com.elhady.lafyuu.feature.checkout.data.mapper.toDomain
import com.elhady.lafyuu.feature.checkout.data.remote.CheckoutApi
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import kotlinx.coroutines.CancellationException
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return AppResult.Error(
            message = networkError.toErrorMessage(),
            throwable = (networkError as? NetworkError.Unknown)?.throwable
        )
    }
}
