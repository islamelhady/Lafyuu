package com.elhady.lafyuu.feature.explore.data.remote

import com.elhady.lafyuu.feature.explore.data.remote.model.GetAllCategoriesResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ExploreApi {
    @GET("api/categories")
    suspend fun getCategories(): Response<GetAllCategoriesResponseDto>
}
