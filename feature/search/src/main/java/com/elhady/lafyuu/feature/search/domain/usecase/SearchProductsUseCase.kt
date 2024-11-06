package com.elhady.lafyuu.feature.search.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.Product
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        searchTerm: String? = null,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        isInStock: Boolean? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int? = 1,
        pageSize: Int? = 20
    ): AppResult<List<Product>> {
        return repository.getProducts(
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
    }
}
