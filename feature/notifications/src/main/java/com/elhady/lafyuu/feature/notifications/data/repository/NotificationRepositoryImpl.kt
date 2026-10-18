package com.elhady.lafyuu.feature.notifications.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.toAppResultError
import com.elhady.lafyuu.feature.notifications.data.mapper.toDomain
import com.elhady.lafyuu.feature.notifications.data.remote.NotificationApi
import com.elhady.lafyuu.feature.notifications.data.remote.model.BulkMarkNotificationsAsReadRequestDto
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val apiErrorParser: ApiErrorParser
) : NotificationRepository {

    override suspend fun getNotifications(includeRead: Boolean, page: Int, pageSize: Int): AppResult<List<Notification>> {
        return try {
            val response = notificationApi.getNotifications(includeRead, page, pageSize)
            if (response.isSuccessful) {
                val body = response.body()
                val items = body?.notifications?.items.orEmpty()
                AppResult.Success(items.map { it.toDomain() })
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            apiErrorParser.parseException(e).toAppResultError()
        }
    }

    override suspend fun markAsRead(notificationId: String): AppResult<Unit> {
        return try {
            val response = notificationApi.markAsRead(notificationId)
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            apiErrorParser.parseException(e).toAppResultError()
        }
    }

    override suspend fun bulkMarkAsRead(notificationIds: List<String>): AppResult<Unit> {
        return try {
            val response = notificationApi.bulkMarkAsRead(BulkMarkNotificationsAsReadRequestDto(notificationIds))
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                apiErrorParser.parseError(response).toAppResultError()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            apiErrorParser.parseException(e).toAppResultError()
        }
    }
}
