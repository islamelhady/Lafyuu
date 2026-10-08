package com.elhady.lafyuu.feature.notifications.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(includeRead: Boolean = true, page: Int = 1, pageSize: Int = 20): AppResult<List<Notification>> {
        return repository.getNotifications(includeRead, page, pageSize)
    }
}
