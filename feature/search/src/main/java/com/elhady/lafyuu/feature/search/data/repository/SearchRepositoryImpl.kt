package com.elhady.lafyuu.feature.search.data.repository

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.search.data.mapper.toDomain
import com.elhady.lafyuu.feature.search.data.remote.SearchApi
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val safeApiCaller: SafeApiCaller
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
        return safeApiCaller.call {
            searchApi.getProducts(
                searchTerm,
                category,
                minPrice,
                maxPrice,
                isInStock,
                sortBy,
                sortOrder,
                page,
                pageSize
            ).items.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getCategories(): AppResult<List<Category>> {
        return safeApiCaller.call { searchApi.getCategories().categories.orEmpty().map { it.toDomain() } }
    }
}
