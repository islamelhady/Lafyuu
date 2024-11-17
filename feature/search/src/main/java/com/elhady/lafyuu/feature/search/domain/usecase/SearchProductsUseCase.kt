package com.elhady.lafyuu.feature.search.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.search.domain.model.ProductsPage
import com.elhady.lafyuu.feature.search.domain.model.SearchFilters
import com.elhady.lafyuu.feature.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        filters: SearchFilters,
        page: Int = FIRST_PAGE
    ): AppResult<ProductsPage> {
        val query = filters.query.trim()
        if (query.isEmpty()) {
            return AppResult.Success(ProductsPage(items = emptyList(), page = page, hasNextPage = false))
        }
        return repository.searchProducts(filters.copy(query = query), page, PAGE_SIZE)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 20
    }
}