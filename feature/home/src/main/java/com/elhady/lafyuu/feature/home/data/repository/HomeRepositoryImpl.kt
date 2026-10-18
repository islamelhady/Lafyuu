package com.elhady.lafyuu.feature.home.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.toAppResultError
import com.elhady.lafyuu.feature.home.data.mapper.toDomain
import com.elhady.lafyuu.feature.home.data.remote.HomeApi
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.CancellationException
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
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getCategories parsing/network failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
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
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getOffers parsing/network failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
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
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("HomeRepository", "getProducts parsing/network failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
        }
    }
}
