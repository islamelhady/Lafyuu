package com.elhady.lafyuu.feature.auth.domain.model

data class User(
    val userId: String,
    val email: String,
    val fullName: String,
    val profilePicture: String? = null
)

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtUtc: String
)

sealed interface AuthError {
    data object InvalidCredentials : AuthError
    data object EmailAlreadyExists : AuthError
    data object InvalidOtp : AuthError
    data object Unauthorized : AuthError
    data object Network : AuthError
    data object Unknown : AuthError
}

sealed interface ValidationResult {
    data object Success : ValidationResult
    sealed interface Error : ValidationResult {
        data object EmailRequired : Error
        data object InvalidEmailFormat : Error
        data object PasswordRequired : Error
        data object PasswordMissingDigit : Error
        data object PasswordMissingUppercase : Error
        data object PasswordMissingSpecialChar : Error
        data object ConfirmPasswordRequired : Error
        data object PasswordMismatch : Error
        data object NameRequired : Error
        data object OtpRequired : Error
    }
}
