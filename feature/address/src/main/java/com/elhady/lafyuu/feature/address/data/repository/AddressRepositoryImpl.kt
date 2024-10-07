package com.elhady.lafyuu.feature.address.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.address.data.mapper.toDomain
import com.elhady.lafyuu.feature.address.data.remote.AddressApi
import com.elhady.lafyuu.feature.address.data.remote.model.CreateAddressRequestDto
import com.elhady.lafyuu.feature.address.data.remote.model.UpdateAddressRequestDto
import com.elhady.lafyuu.feature.address.domain.model.Address
import com.elhady.lafyuu.feature.address.domain.repository.AddressRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApi: AddressApi,
    private val apiErrorParser: ApiErrorParser
) : AddressRepository {

    override suspend fun getAddresses(): AppResult<List<Address>> {
        return try {
            val response = addressApi.getAddresses()
            if (response.isSuccessful) {
                val list = response.body().orEmpty().map { it.toDomain() }
                AppResult.Success(list)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getAddress(addressId: String): AppResult<Address> {
        return try {
            val response = addressApi.getAddress(addressId)
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
            if (e is CancellationException) throw e
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
            val response = addressApi.createAddress(
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
            if (e is CancellationException) throw e
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
            val response = addressApi.updateAddress(
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
            if (e is CancellationException) throw e
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun deleteAddress(addressId: String): AppResult<Unit> {
        return try {
            val response = addressApi.deleteAddress(addressId)
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
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
