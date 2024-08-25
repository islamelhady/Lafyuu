package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.ProductReviewsInfo
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductReviewsInfoUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String, page: Int = 1, pageSize: Int = 50): AppResult<ProductReviewsInfo> {
        if (productId.isBlank()) {
            return AppResult.Error("Invalid product ID")
        }
        return repository.getProductReviewsInfo(productId, page, pageSize)
    }
}
