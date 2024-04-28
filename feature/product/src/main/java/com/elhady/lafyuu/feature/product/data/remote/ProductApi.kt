package com.elhady.lafyuu.feature.product.data.remote

import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartRequestDto
import com.elhady.lafyuu.feature.product.data.remote.model.AddItemToCartResponseDto
import com.elhady.lafyuu.feature.product.data.remote.model.GetProductReviewsResponseDto
import com.elhady.lafyuu.feature.product.data.remote.model.PagedListOfProductDto
import com.elhady.lafyuu.feature.product.data.remote.model.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("api/products/{Id}")
    suspend fun getProductDetails(
        @Path("Id") id: String
    ): Response<ProductDto>

    @GET("api/reviews/{productId}")
    suspend fun getProductReviews(
        @Path("productId") productId: String,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Response<GetProductReviewsResponseDto>

    @POST("api/cart/items")
    suspend fun addItemToCart(
        @Body request: AddItemToCartRequestDto
    ): Response<AddItemToCartResponseDto>

    @GET("api/products")
    suspend fun getProducts(
        @Query("searchTerm") searchTerm: String? = null,
        @Query("category") category: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Response<PagedListOfProductDto>
}
