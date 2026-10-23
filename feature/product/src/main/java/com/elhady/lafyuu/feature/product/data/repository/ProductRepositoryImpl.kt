package com.elhady.lafyuu.feature.product.data.repository

import com.elhady.lafyuu.core.common.domain.model.Product
import com.elhady.lafyuu.core.common.domain.model.ProductDetails
import com.elhady.lafyuu.core.common.domain.model.ProductReview
import com.elhady.lafyuu.core.common.domain.repository.ProductRepository
import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.product.data.mapper.toDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductDetailsDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductSummaryDomain
import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartRequestDto
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val safeApiCaller: SafeApiCaller
) : ProductRepository {

    override suspend fun getProductDetails(id: String): AppResult<ProductDetails> {
        return safeApiCaller.call { productApi.getProductDetails(id).toProductDetailsDomain() }
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
            ).reviews?.items.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun getRecommendedProducts(pageSize: Int): AppResult<List<Product>> {
        return safeApiCaller.call {
            productApi.getProducts(pageSize = pageSize).items.orEmpty()
                .map { it.toProductSummaryDomain() }
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int): AppResult<Unit> {
        return safeApiCaller.call {
            productApi.addItemToCart(
                AddItemToCartRequestDto(
                    productId = productId,
                    quantity = quantity
                )
            )
        }
    }
}
