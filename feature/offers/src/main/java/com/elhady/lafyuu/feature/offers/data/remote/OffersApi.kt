package com.elhady.lafyuu.feature.offers.data.remote

import com.elhady.lafyuu.feature.offers.data.remote.model.GetAllOffersResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OffersApi {
    @GET("api/offers")
    suspend fun getOffers(
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 50
    ): Response<GetAllOffersResponseDto>
}
