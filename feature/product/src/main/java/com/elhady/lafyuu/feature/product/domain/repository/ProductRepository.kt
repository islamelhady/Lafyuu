package com.elhady.lafyuu.feature.product.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.model.ProductReviewsInfo

interface ProductRepository {
    suspend fun getProductDetails(id: String): AppResult<ProductDetails>
    suspend fun getProductReviews(productId: String, page: Int = 1, pageSize: Int = 10): AppResult<List<ProductReview>>
    suspend fun getProductReviewsInfo(productId: String, page: Int = 1, pageSize: Int = 50): AppResult<ProductReviewsInfo>
    suspend fun getRecommendedProducts(pageSize: Int = 10): AppResult<List<Product>>
    suspend fun addToCart(productId: String, quantity: Int): AppResult<Unit>
}
