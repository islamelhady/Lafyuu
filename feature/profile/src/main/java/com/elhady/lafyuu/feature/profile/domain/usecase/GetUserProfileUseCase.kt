package com.elhady.lafyuu.feature.profile.domain.usecase

import com.elhady.lafyuu.core.common.result.AppResult
import com.elhady.lafyuu.feature.auth.domain.model.User
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): AppResult<User> {
        return repository.getUserProfile()
    }
}
