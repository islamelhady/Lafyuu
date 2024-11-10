package com.elhady.lafyuu.feature.product.domain.usecase

//import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.models.ProductContent
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): AppResult<ProductContent> = coroutineScope {
        val details = async { repository.getProductDetails(productId) }
        val reviews = async { repository.getProductReviews(productId, page = 1, pageSize = REVIEWS_PREVIEW_COUNT) }
        val recommended = async { repository.getRecommendedProducts(pageSize = RECOMMENDED_COUNT + 1) }

        when (val detailsResult = details.await()) {
            is AppResult.Failure -> detailsResult
            is AppResult.Success -> AppResult.Success(
                ProductContent(
                    details = detailsResult.data,
                    reviews = reviews.await().orEmptyList(),
                    recommendedProducts = recommended.await().orEmptyList()
                        .filter { it.id != productId }
                        .take(RECOMMENDED_COUNT)
                )
            )
        }
    }

    private fun <T> AppResult<List<T>>.orEmptyList(): List<T> =
        (this as? AppResult.Success)?.data.orEmpty()

    private companion object {
        const val REVIEWS_PREVIEW_COUNT = 5
        const val RECOMMENDED_COUNT = 10
    }
}