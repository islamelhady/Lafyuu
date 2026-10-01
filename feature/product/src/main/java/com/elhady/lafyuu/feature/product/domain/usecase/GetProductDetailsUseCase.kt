package com.elhady.lafyuu.feature.product.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

data class ProductContent(
    val details: ProductDetails,
    val reviews: List<ProductReview> = emptyList(),
    val recommendedProducts: List<Product> = emptyList()
)

class GetProductDetailsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): AppResult<ProductContent> = coroutineScope {
        if (productId.isBlank()) {
            return@coroutineScope AppResult.Error("Invalid product ID")
        }

        val detailsDeferred = async { repository.getProductDetails(productId) }
        val reviewsDeferred = async { repository.getProductReviews(productId, page = 1, pageSize = 5) }
        val recommendedDeferred = async { repository.getRecommendedProducts(pageSize = 10) }

        val detailsResult = detailsDeferred.await()
        val reviewsResult = reviewsDeferred.await()
        val recommendedResult = recommendedDeferred.await()

        when (detailsResult) {
            is AppResult.Success -> {
                val reviewsList = when (reviewsResult) {
                    is AppResult.Success -> reviewsResult.data
                    else -> emptyList()
                }

                val recommendedList = when (recommendedResult) {
                    is AppResult.Success -> recommendedResult.data.filter { it.id != productId }
                    else -> emptyList()
                }

                AppResult.Success(
                    ProductContent(
                        details = detailsResult.data,
                        reviews = reviewsList,
                        recommendedProducts = recommendedList
                    )
                )
            }
            is AppResult.Error -> detailsResult
            is AppResult.Loading -> AppResult.Loading
        }
    }
}
