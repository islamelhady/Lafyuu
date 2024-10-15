package com.elhady.lafyuu.feature.search.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.core.network.error.toErrorMessage
import com.elhady.lafyuu.feature.search.data.mapper.toDomain
import com.elhady.lafyuu.feature.search.data.remote.SearchApi
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val apiErrorParser: ApiErrorParser
) : SearchRepository {

    override suspend fun getProducts(
        searchTerm: String?,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        isInStock: Boolean?,
        sortBy: String?,
        sortOrder: String?,
        page: Int?,
        pageSize: Int?
    ): AppResult<List<Product>> {
        return try {
            val response = searchApi.getProducts(
                searchTerm = searchTerm,
                category = category,
                minPrice = minPrice,
                maxPrice = maxPrice,
                isInStock = isInStock,
                sortBy = sortBy,
                sortOrder = sortOrder,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.items.orEmpty().map { it.toDomain() })
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

    override suspend fun getCategories(): AppResult<List<Category>> {
        return try {
            val response = searchApi.getCategories()
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
        return AppResult.Error(
            message = networkError.toErrorMessage(),
            throwable = (networkError as? NetworkError.Unknown)?.throwable
        )
    }
}
