package com.elhady.lafyuu.feature.notifications.data.repository

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.core.network.error.NetworkError
import com.elhady.lafyuu.feature.notifications.data.mapper.toDomain
import com.elhady.lafyuu.feature.notifications.data.remote.NotificationApi
import com.elhady.lafyuu.feature.notifications.data.remote.model.BulkMarkNotificationsAsReadRequestDto
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
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
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun markAsRead(notificationId: String): AppResult<Unit> {
        return try {
            val response = notificationApi.markAsRead(notificationId)
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    override suspend fun bulkMarkAsRead(notificationIds: List<String>): AppResult<Unit> {
        return try {
            val response = notificationApi.bulkMarkAsRead(BulkMarkNotificationsAsReadRequestDto(notificationIds))
            if (response.isSuccessful) {
                AppResult.Success(Unit)
            } else {
                mapError(apiErrorParser.parseError(response))
            }
        } catch (e: Exception) {
            mapError(apiErrorParser.parseException(e))
        }
    }

    private fun mapError(networkError: NetworkError): AppResult.Error {
        return when (networkError) {
            is NetworkError.ApiError -> {
                val details = networkError.details
                val errorMessage = details.detail
                    ?: details.title
                    ?: details.errors?.values?.flatten()?.firstOrNull()
                    ?: "Request failed"
                AppResult.Error(message = errorMessage)
            }
            is NetworkError.Connectivity -> AppResult.Error(message = "No internet connection")
            is NetworkError.Serialization -> AppResult.Error(message = "Server error (Parsing)")
            is NetworkError.Server -> AppResult.Error(message = "Internal server error")
            is NetworkError.Unknown -> AppResult.Error(
                message = networkError.throwable.localizedMessage ?: "An unexpected error occurred",
                throwable = networkError.throwable
            )
        }
    }
}
