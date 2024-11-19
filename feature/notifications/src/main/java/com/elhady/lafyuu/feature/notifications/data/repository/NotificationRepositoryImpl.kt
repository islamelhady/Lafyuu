package com.elhady.lafyuu.feature.notifications.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.notifications.data.mapper.toDomain
import com.elhady.lafyuu.feature.notifications.data.remote.NotificationApi
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val safeApiCaller: SafeApiCaller
) : NotificationRepository {

    override suspend fun getNotifications(page: Int, pageSize: Int): AppResult<NotificationsPage> =
        safeApiCaller.call {
            notificationApi.getNotifications(includeRead = true, page = page, pageSize = pageSize)
                .notifications
                ?.toDomain(requestedPage = page)
                ?: NotificationsPage(items = emptyList(), page = page, hasNextPage = false)
        }

    override suspend fun markAsRead(notificationId: String): AppResult<Unit> =
        safeApiCaller.call {
            notificationApi.markAsRead(notificationId)
        }
}