package com.elhady.lafyuu.feature.auth.data.remote

import com.elhady.lafyuu.feature.auth.data.remote.model.ForgotPasswordRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.LoginRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.LoginResponse
import com.elhady.lafyuu.feature.auth.data.remote.model.MobileLoginRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.RefreshTokenResponse
import com.elhady.lafyuu.feature.auth.data.remote.model.RegisterRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ResendOtpRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.ResetPasswordRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.UserInfoResponse
import com.elhady.lafyuu.feature.auth.data.remote.model.ValidateOtpRequest
import com.elhady.lafyuu.feature.auth.data.remote.model.VerifyEmailRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("api/auth/google/mobile")
    suspend fun googleLogin(@Body request: MobileLoginRequest): Response<LoginResponse>

    @POST("api/auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<Unit>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(@Body request: ResendOtpRequest): Response<Unit>

    @POST("api/auth/validate-otp")
    suspend fun validateOtp(@Body request: ValidateOtpRequest): Response<Unit>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>

    @GET("api/auth/me")
    suspend fun getCurrentUser(): Response<UserInfoResponse>

    @POST("api/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
}
