package com.elhady.lafyuu.feature.search.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.Product

interface SearchRepository {
    suspend fun getProducts(
        searchTerm: String? = null,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        isInStock: Boolean? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int? = 1,
        pageSize: Int? = 20
    ): AppResult<List<Product>>

    suspend fun getCategories(): AppResult<List<Category>>
}
