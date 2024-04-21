package com.elhady.lafyuu.feature.home.data.remote

import com.elhady.lafyuu.feature.home.data.remote.model.GetAllCategoriesResponse
import com.elhady.lafyuu.feature.home.data.remote.model.GetAllOffersResponse
import com.elhady.lafyuu.feature.home.data.remote.model.PagedListOfProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("api/categories")
    suspend fun getCategories(): Response<GetAllCategoriesResponse>

    @GET("api/products")
    suspend fun getProducts(
        @Query("searchTerm") searchTerm: String? = null,
        @Query("category") category: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("isInStock") isInStock: Boolean? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Response<PagedListOfProductDto>

    @GET("api/offers")
    suspend fun getOffers(
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Response<GetAllOffersResponse>
}
