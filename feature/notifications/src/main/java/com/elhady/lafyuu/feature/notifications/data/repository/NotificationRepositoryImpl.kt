package com.elhady.lafyuu.feature.notifications.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.notifications.data.mapper.toDomain
import com.elhady.lafyuu.feature.notifications.data.remote.NotificationApi
import com.elhady.lafyuu.feature.notifications.data.remote.model.BulkMarkNotificationsAsReadRequestDto
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val safeApiCaller: SafeApiCaller,
) : NotificationRepository {

    override suspend fun getNotifications(
        includeRead: Boolean,
        page: Int,
        pageSize: Int
    ): AppResult<List<Notification>> {
        return safeApiCaller.call {
            notificationApi.getNotifications(
                includeRead,
                page,
                pageSize
            ).notifications?.items.orEmpty().map { it.toDomain() }
        }
    }

    override suspend fun markAsRead(notificationId: String): AppResult<Unit> {
        return safeApiCaller.call { notificationApi.markAsRead(notificationId) }
    }


    override suspend fun bulkMarkAsRead(notificationIds: List<String>): AppResult<Unit> {
        return safeApiCaller.call {
            notificationApi.bulkMarkAsRead(
                BulkMarkNotificationsAsReadRequestDto(notificationIds)
            )
        }
    }
}
