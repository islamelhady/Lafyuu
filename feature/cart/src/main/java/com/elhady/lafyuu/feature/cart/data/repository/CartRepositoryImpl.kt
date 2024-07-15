package com.elhady.lafyuu.feature.cart.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.cart.data.mapper.*
import com.elhady.lafyuu.feature.cart.data.remote.CartApi
import com.elhady.lafyuu.feature.cart.data.remote.model.ApplyCouponRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DecrementItemRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.DeleteItemFromCartRequestDto
import com.elhady.lafyuu.feature.cart.data.remote.model.UpdateItemRequestDto
import com.elhady.lafyuu.feature.cart.domain.model.Cart
import com.elhady.lafyuu.feature.cart.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi,
    private val apiErrorParser: ApiErrorParser
) : CartRepository {

    override suspend fun getCart(): AppResult<Cart> {
        return try {
            val response = cartApi.getCart()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Empty response body")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun updateItemQuantity(itemId: String, quantity: Int): AppResult<Unit> {
        return try {
            val response = cartApi.updateCartItem(itemId, UpdateItemRequestDto(quantity))
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun decrementItem(itemId: String, quantity: Int): AppResult<Unit> {
        return try {
            val response = cartApi.decrementCartItem(DecrementItemRequestDto(itemId, quantity))
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun removeItem(itemId: String): AppResult<Unit> {
        return try {
            val response = cartApi.deleteCartItem(itemId, DeleteItemFromCartRequestDto(itemId))
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun applyCoupon(couponCode: String): AppResult<Cart> {
        return try {
            val response = cartApi.applyCoupon(ApplyCouponRequestDto(couponCode))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Empty response body")
                }
            } else {
                val networkError = apiErrorParser.parseError(response)
                if (response.code() == 400) {
                    AppResult.Error(message = "Your Cupon Is Not Correct")
                } else {
                    mapError(networkError)
                }
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
