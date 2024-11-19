package com.elhady.lafyuu.feature.notifications.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): AppResult<Unit> {
        if (notificationId.isBlank()) {
            return AppResult.Failure(AppError.Validation("Invalid notification ID"))
        }
        return repository.markAsRead(notificationId)
    }
}