package com.elhady.lafyuu.feature.search.data.remote

import com.elhady.lafyuu.feature.search.data.remote.model.GetAllCategoriesResponseDto
import com.elhady.lafyuu.feature.search.data.remote.model.PagedListOfProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
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
        @Query("pageSize") pageSize: Int? = 20
    ): Response<PagedListOfProductDto>

    @GET("api/categories")
    suspend fun getCategories(): Response<GetAllCategoriesResponseDto>
}
