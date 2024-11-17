package com.elhady.lafyuu.feature.search.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.search.data.mapper.toApiParams
import com.elhady.lafyuu.feature.search.data.mapper.toDomain
import com.elhady.lafyuu.feature.search.data.remote.SearchApi
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val safeApiCaller: SafeApiCaller
) : SearchRepository {

    override suspend fun searchProducts(
        filters: SearchFilters,
        page: Int,
        pageSize: Int
    ): AppResult<ProductsPage> = safeApiCaller.call {
        val sort = filters.sort?.toApiParams()
        searchApi.getProducts(
            searchTerm = filters.query,
            category = filters.category,
            minPrice = filters.minPrice,
            maxPrice = filters.maxPrice,
            isInStock = if (filters.inStockOnly) true else null,
            sortBy = sort?.first,
            sortOrder = sort?.second,
            page = page,
            pageSize = pageSize
        ).toDomain(requestedPage = page)
    }

    override suspend fun getCategories(): AppResult<List<Category>> = safeApiCaller.call {
        searchApi.getCategories().categories.orEmpty()
            .map { it.toDomain() }
            .filter { it.name.isNotBlank() }
    }
}