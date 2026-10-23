package com.elhady.lafyuu.feature.profile.domain.models

data class User(
    val userId: String,
    val email: String,
    val fullName: String,
    val profilePicture: String? = null
)

data class ChangePassword(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)