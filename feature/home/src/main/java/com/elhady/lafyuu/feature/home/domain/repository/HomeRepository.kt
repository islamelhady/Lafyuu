package com.elhady.lafyuu.feature.home.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Category
import com.elhady.lafyuu.feature.home.domain.model.Offer
import com.elhady.lafyuu.feature.home.domain.model.Product

interface HomeRepository {
    suspend fun getCategories(): AppResult<List<Category>>
    suspend fun getOffers(): AppResult<List<Offer>>
    suspend fun getProducts(
        searchTerm: String? = null,
        category: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int = 1,
        pageSize: Int = 10
    ): AppResult<List<Product>>
}
