package com.elhady.lafyuu.feature.search.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.Category
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters

interface SearchRepository {
    suspend fun searchProducts(filters: SearchFilters, page: Int, pageSize: Int): AppResult<ProductsPage>
    suspend fun getCategories(): AppResult<List<Category>>
}
