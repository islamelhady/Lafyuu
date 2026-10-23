package com.elhady.lafyuu.feature.auth.data.remote

import com.elhady.lafyuu.feature.auth.data.remote.model.ChangePasswordRequest
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
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("api/auth/google/mobile")
    suspend fun googleLogin(@Body request: MobileLoginRequest): LoginResponse

    @POST("api/auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest)

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest)

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest)

    @POST("api/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest)

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(@Body request: ResendOtpRequest)

    @POST("api/auth/validate-otp")
    suspend fun validateOtp(@Body request: ValidateOtpRequest)

    @GET("api/auth/me")
    suspend fun getCurrentUser(): UserInfoResponse
}
