package com.elhady.lafyuu.feature.offers.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.offers.data.mapper.toDomain
import com.elhady.lafyuu.feature.offers.data.remote.OffersApi
import com.elhady.lafyuu.feature.offers.domain.model.Offer
import com.elhady.lafyuu.feature.offers.domain.repository.OffersRepository
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val offersApi: OffersApi,
    private val apiErrorParser: ApiErrorParser
) : OffersRepository {

    override suspend fun getOffers(): AppResult<List<Offer>> {
        return try {
            val response = offersApi.getOffers()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val offerDtos = body.offers?.items ?: body.allOffers
                    AppResult.Success(offerDtos.map { it.toDomain() })
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
