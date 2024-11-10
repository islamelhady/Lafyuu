package com.elhady.lafyuu.feature.product.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.model.RecommendedProduct

interface ProductRepository {
    suspend fun getProductDetails(id: String): AppResult<ProductDetails>
    suspend fun getProductReviews(productId: String, page: Int, pageSize: Int): AppResult<List<ProductReview>>
    suspend fun getRecommendedProducts(pageSize: Int): AppResult<List<RecommendedProduct>>

    /** Returns the new cart item id when the server sends one. */
    suspend fun addToCart(productId: String, quantity: Int): AppResult<String?>
}