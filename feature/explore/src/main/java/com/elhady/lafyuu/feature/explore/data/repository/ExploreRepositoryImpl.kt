package com.elhady.lafyuu.feature.explore.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.explore.data.mapper.toDomain
import com.elhady.lafyuu.feature.explore.data.remote.ExploreApi
import com.elhady.lafyuu.feature.explore.domain.model.Category
import com.elhady.lafyuu.feature.explore.domain.repository.ExploreRepository
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val exploreApi: ExploreApi,
    private val apiErrorParser: ApiErrorParser
) : ExploreRepository {

    override suspend fun getCategories(): AppResult<List<Category>> {
        return try {
            val response = exploreApi.getCategories()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.allCategories.map { it.toDomain() })
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
