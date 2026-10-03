package com.elhady.lafyuu.feature.checkout.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.checkout.data.mapper.toDomain
import com.elhady.lafyuu.feature.checkout.data.remote.CheckoutApi
import com.elhady.lafyuu.feature.checkout.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.OrderCheckoutRequestDto
import com.elhady.lafyuu.feature.checkout.data.remote.model.UpdateAddressRequestDto
import com.elhady.lafyuu.feature.checkout.domain.model.Address
import com.elhady.lafyuu.feature.checkout.domain.model.CheckoutResult
import com.elhady.lafyuu.feature.checkout.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val checkoutApi: CheckoutApi,
    private val apiErrorParser: ApiErrorParser
) : CheckoutRepository {

    override suspend fun getAddresses(): AppResult<List<Address>> {
        return try {
            val response = checkoutApi.getAddresses()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.map { it.toDomain() })
                } else {
                    AppResult.Success(emptyList())
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getAddress(addressId: String): AppResult<Address> {
        return try {
            val response = checkoutApi.getAddress(addressId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Address not found")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun createAddress(
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        return try {
            val response = checkoutApi.createAddress(
                CreateAddressRequestDto(
                    state = state,
                    city = city,
                    street = street,
                    apartment = apartment,
                    phoneNumber = phoneNumber,
                    notes = notes
                )
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Failed to create address")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun updateAddress(
        addressId: String,
        state: String,
        city: String,
        street: String,
        apartment: String,
        phoneNumber: String,
        notes: String
    ): AppResult<Address> {
        return try {
            val response = checkoutApi.updateAddress(
                addressId,
                UpdateAddressRequestDto(
                    id = addressId,
                    state = state,
                    city = city,
                    street = street,
                    apartment = apartment,
                    phoneNumber = phoneNumber,
                    notes = notes
                )
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toDomain())
                } else {
                    AppResult.Error(message = "Failed to update address")
                }
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun deleteAddress(addressId: String): AppResult<Unit> {
        return try {
            val response = checkoutApi.deleteAddress(addressId)
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

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
