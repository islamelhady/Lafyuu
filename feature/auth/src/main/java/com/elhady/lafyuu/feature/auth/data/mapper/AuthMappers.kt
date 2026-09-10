package com.elhady.lafyuu.feature.auth.data.mapper

import com.elhady.lafyuu.feature.auth.data.remote.model.LoginResponse
import com.elhady.lafyuu.feature.auth.data.remote.model.UserInfoResponse
import com.elhady.lafyuu.feature.auth.domain.model.AuthToken
import com.elhady.lafyuu.feature.auth.domain.model.User

fun LoginResponse.toDomain() = AuthToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresAtUtc = expiresAtUtc
)

fun UserInfoResponse.toDomain() = User(
    userId = userId,
    email = email,
    fullName = fullName,
    profilePicture = profilePicture
)
