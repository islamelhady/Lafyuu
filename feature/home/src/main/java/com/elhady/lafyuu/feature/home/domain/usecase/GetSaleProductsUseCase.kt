package com.elhady.lafyuu.feature.home.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.home.domain.model.PRODUCTS_POOL_SIZE
import com.elhady.lafyuu.feature.home.domain.model.Product
import com.elhady.lafyuu.feature.home.domain.model.ProductQuery
import com.elhady.lafyuu.feature.home.domain.model.SaleSection
import com.elhady.lafyuu.feature.home.domain.model.toSaleSections
import com.elhady.lafyuu.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetSaleProductsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(section: SaleSection): AppResult<List<Product>> =
        when (val result = homeRepository.getProducts(ProductQuery(pageSize = PRODUCTS_POOL_SIZE))) {
            is AppResult.Success -> {
                val sections = result.data.toSaleSections()
                AppResult.Success(if (section == SaleSection.Flash) sections.flash else sections.mega)
            }
            is AppResult.Failure -> AppResult.Failure(error = result.error)
        }
}