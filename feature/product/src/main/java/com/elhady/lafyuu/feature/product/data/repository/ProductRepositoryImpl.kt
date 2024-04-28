package com.elhady.lafyuu.feature.product.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.product.data.mapper.toDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductDetailsDomain
import com.elhady.lafyuu.feature.product.data.mapper.toProductSummaryDomain
import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartRequestDto
import com.elhady.lafyuu.feature.product.domain.model.Product
import com.elhady.lafyuu.feature.product.domain.model.ProductDetails
import com.elhady.lafyuu.feature.product.domain.model.ProductReview
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
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
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getProductDetails failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
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
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getProductReviews failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
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
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "getRecommendedProducts failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
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
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductRepository", "addToCart failure: ${e.message}", e)
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
