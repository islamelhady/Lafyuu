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
