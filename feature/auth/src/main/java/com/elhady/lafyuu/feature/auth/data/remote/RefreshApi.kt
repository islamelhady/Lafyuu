package com.elhady.lafyuu.feature.auth.data.remote

import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("api/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
}
