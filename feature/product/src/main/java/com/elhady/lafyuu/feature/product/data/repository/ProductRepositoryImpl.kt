package com.elhady.lafyuu.feature.product.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.product.data.mapper.toProductDetails
import com.elhady.lafyuu.feature.product.data.mapper.toRecommendedProduct
import com.elhady.lafyuu.feature.product.data.mapper.toReview
import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartRequestDto
import com.elhady.lafyuu.feature.product.domain.models.ProductDetails
import com.elhady.lafyuu.feature.product.domain.models.ProductReview
import com.elhady.lafyuu.feature.product.domain.models.RecommendedProduct
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val safeApiCaller: SafeApiCaller
) : ProductRepository {

    override suspend fun getProductDetails(id: String): AppResult<ProductDetails> {
        return safeApiCaller.call { productApi.getProductDetails(id).toProductDetails() }
    }

    override suspend fun getProductReviews(
        productId: String,
        page: Int,
        pageSize: Int
    ): AppResult<List<ProductReview>> {
        return safeApiCaller.call {
            productApi.getProductReviews(
                productId,
                page,
                pageSize
            ).reviews?.items.orEmpty().map { it.toReview() }
        }
    }

    override suspend fun getRecommendedProducts(pageSize: Int): AppResult<List<RecommendedProduct>> {
        return safeApiCaller.call {
            productApi.getProducts(pageSize = pageSize).items.orEmpty()
                .map { it.toRecommendedProduct() }
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int): AppResult<String?> {
        return safeApiCaller.call {
            productApi.addItemToCart(
                AddItemToCartRequestDto(
                    productId = productId,
                    quantity = quantity
                )
            ).id
        }
    }
}
