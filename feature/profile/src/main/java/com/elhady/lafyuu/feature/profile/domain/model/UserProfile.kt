package com.elhady.lafyuu.feature.profile.domain.model

data class UserProfile(
    val userId: String,
    val email: String,
    val fullName: String,
    val profilePicture: String?
)
