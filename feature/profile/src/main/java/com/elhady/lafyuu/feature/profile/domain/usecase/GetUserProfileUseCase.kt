package com.elhady.lafyuu.feature.profile.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.profile.domain.model.UserProfile
import com.elhady.lafyuu.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): AppResult<UserProfile> {
        return repository.getUserProfile()
    }
}
