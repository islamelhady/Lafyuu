package com.elhady.lafyuu.feature.explore.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.error.toErrorMessage
import com.elhady.lafyuu.feature.explore.data.mapper.toDomain
import com.elhady.lafyuu.feature.explore.data.remote.ExploreApi
import com.elhady.lafyuu.feature.explore.domain.model.Category
import com.elhady.lafyuu.feature.explore.domain.repository.ExploreRepository
import kotlinx.coroutines.CancellationException
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> AppResult.Error(
                message = networkError.toErrorMessage(),
                status = networkError.details.status,
                validationErrors = networkError.details.errors
            )
            is NetworkError.Connectivity -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Serialization -> AppResult.Error(message = networkError.toErrorMessage())
            is NetworkError.Unknown -> AppResult.Error(message = networkError.toErrorMessage(), throwable = networkError.throwable)
        }
    }
}
