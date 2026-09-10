package com.elhady.lafyuu.feature.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val accessToken: String,
    val expiresAtUtc: String,
    val refreshToken: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String,
    val useCookies: Boolean = false
)

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val expiresAtUtc: String,
    val refreshToken: String
)

@Serializable
data class MobileLoginRequest(
    val idToken: String
)

@Serializable
data class VerifyEmailRequest(
    val email: String,
    val otp: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)

@Serializable
data class ValidateOtpRequest(
    val email: String,
    val otp: String
)

@Serializable
data class ResendOtpRequest(
    val email: String
)

@Serializable
data class UserInfoResponse(
    val userId: String,
    val email: String,
    val fullName: String,
    val profilePicture: String? = null
)
