package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(
        searchTerm: String? = null,
        category: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        page: Int = 1,
        pageSize: Int = 10
    ): AppResult<List<Product>> {
        return homeRepository.getProducts(
            searchTerm = searchTerm,
            category = category,
            sortBy = sortBy,
            sortOrder = sortOrder,
            page = page,
            pageSize = pageSize
        )
    }
}
