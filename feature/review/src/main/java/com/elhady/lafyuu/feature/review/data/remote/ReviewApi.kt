package com.elhady.lafyuu.feature.review.data.remote

import com.elhady.lafyuu.feature.review.data.remote.model.CreateReviewRequestDto
import com.elhady.lafyuu.feature.review.data.remote.model.CreateReviewResponseDto
import com.elhady.lafyuu.feature.review.data.remote.model.GetProductReviewsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {

    @GET("api/reviews/{productId}")
    suspend fun getProductReviews(
        @Path("productId") productId: String,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 10
    ): Response<GetProductReviewsResponseDto>

    @POST("api/reviews/{productId}")
    suspend fun createReview(
        @Path("productId") productId: String,
        @Body request: CreateReviewRequestDto
    ): Response<CreateReviewResponseDto>
}
