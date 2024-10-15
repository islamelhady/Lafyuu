package com.elhady.lafyuu.feature.product.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.domain.model.Product
import com.elhady.lafyuu.core.common.domain.model.ProductDetails
import com.elhady.lafyuu.core.common.domain.model.ProductReview
import com.elhady.lafyuu.core.common.domain.repository.ProductRepository
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.toAppResultError
import com.elhady.lafyuu.feature.product.data.mapper.toDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductDetailsDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductSummaryDomain
import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartRequestDto
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val apiErrorParser: ApiErrorParser
) : ProductRepository {

    override suspend fun getProductDetails(id: String): AppResult<ProductDetails> {
        return try {
            val response = productApi.getProductDetails(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AppResult.Success(body.toProductDetailsDomain())
                } else {
                    AppResult.Error(message = "Product details not found")
                }
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getProductDetails failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
        }
    }

    override suspend fun getProductReviews(
        productId: String,
        page: Int,
        pageSize: Int
    ): AppResult<List<ProductReview>> {
        return try {
            val response = productApi.getProductReviews(
                productId = productId,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val body = response.body()
                val reviews = body?.reviews?.items.orEmpty().map { it.toDomain() }
                AppResult.Success(reviews)
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getProductReviews failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
        }
    }

    override suspend fun getRecommendedProducts(pageSize: Int): AppResult<List<Product>> {
        return try {
            val response = productApi.getProducts(pageSize = pageSize)
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.allProducts.orEmpty().map { it.toProductSummaryDomain() }
                AppResult.Success(list)
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getRecommendedProducts failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int): AppResult<Unit> {
        return try {
            val response = productApi.addItemToCart(
                AddItemToCartRequestDto(
                    productId = productId,
                    quantity = quantity
                )
            )
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "addToCart failure: ${e.message}", e)
            apiErrorParser.parseException(e).toAppResultError()
        }
    }
}
