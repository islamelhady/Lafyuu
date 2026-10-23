package com.elhady.lafyuu.feature.profile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userId: String,
    val email: String,
    val fullName: String,
    val profilePicture: String? = null
)

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)