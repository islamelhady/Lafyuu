package com.elhady.lafyuu.feature.notifications.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.notifications.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(includeRead: Boolean = true, page: Int = 1, pageSize: Int = 20): AppResult<List<Notification>>
    suspend fun markAsRead(notificationId: String): AppResult<Unit>
    suspend fun bulkMarkAsRead(notificationIds: List<String>): AppResult<Unit>
}
