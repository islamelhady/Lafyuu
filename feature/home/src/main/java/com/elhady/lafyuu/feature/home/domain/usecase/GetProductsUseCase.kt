package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
suspend operator fun invoke(query: ProductQuery): AppResult<List<Product>> {
        return homeRepository.getProducts(query)
    }
}
