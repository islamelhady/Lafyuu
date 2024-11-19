package com.elhady.lafyuu.feature.notifications.domain.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage

interface NotificationRepository {
    suspend fun getNotifications(page: Int, pageSize: Int): AppResult<NotificationsPage>
    suspend fun markAsRead(notificationId: String): AppResult<Unit>
}