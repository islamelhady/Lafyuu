package com.elhady.lafyuu.feature.home.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.home.data.mapper.toDomain
import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val apiErrorParser: ApiErrorParser
) : HomeRepository {

    override suspend fun getCategories(): AppResult<List<Category>> {
        return try {
            val response = homeApi.getCategories()
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.allCategories ?: emptyList()
                AppResult.Success(list.map { it.toDomain() })
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getCategories parsing/network failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getOffers(): AppResult<List<Offer>> {
        return try {
            val response = homeApi.getOffers()
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.allOffers ?: emptyList()
                AppResult.Success(list.map { it.toDomain() })
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getOffers parsing/network failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun getProducts(
        searchTerm: String?,
        category: String?,
        sortBy: String?,
        sortOrder: String?,
        page: Int,
        pageSize: Int
    ): AppResult<List<Product>> {
        return try {
            val response = homeApi.getProducts(
                searchTerm = searchTerm,
                category = category,
                sortBy = sortBy,
                sortOrder = sortOrder,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.allProducts ?: emptyList()
                AppResult.Success(list.map { it.toDomain() })
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getProducts parsing/network failure: ${e.message}", e)
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
