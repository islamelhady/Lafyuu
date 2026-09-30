package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(query: String, pageSize: Int = 20): AppResult<List<Product>> {
        if (query.isBlank()) {
            return AppResult.Success(emptyList())
        }
        return homeRepository.getProducts(searchTerm = query, pageSize = pageSize)
    }
}
