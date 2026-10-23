package com.elhady.lafyuu.feature.profile.data.mapper

import com.elhady.lafyuu.feature.profile.data.remote.model.ChangePasswordRequest
import com.elhady.lafyuu.feature.profile.data.remote.model.UserInfoResponse
import com.elhady.lafyuu.feature.profile.domain.models.ChangePassword
import com.elhady.lafyuu.feature.profile.domain.models.User

fun UserInfoResponse.toDomain(): User {
    return User(
        userId = userId,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture
    )
}

fun ChangePasswordRequest.toDomain(): ChangePassword {
     return ChangePassword(
        currentPassword = currentPassword,
        newPassword = newPassword,
        confirmNewPassword = confirmNewPassword
    )
}