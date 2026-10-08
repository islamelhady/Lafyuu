package com.elhady.lafyuu.feature.notifications.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: String? = null,
    val notificationText: String? = null,
    val createdAt: String? = null,
    val isRead: Boolean? = false
)

@Serializable
data class PagedListOfNotificationDto(
    val items: List<NotificationDto>? = emptyList(),
    val page: Int? = 1,
    val pageSize: Int? = 20,
    val totalCount: Int? = 0,
    val hasNextPage: Boolean? = false,
    val hasPreviousPage: Boolean? = false
)

@Serializable
data class GetUserNotificationsResponseDto(
    val notifications: PagedListOfNotificationDto? = null
)

@Serializable
data class GetUserNotificationsRequestDto(
    val includeRead: Boolean? = true,
    val page: Int? = 1,
    val pageSize: Int? = 20
)

@Serializable
data class MarkNotificationAsReadResponseDto(
    val message: String? = null
)

@Serializable
data class BulkMarkNotificationsAsReadRequestDto(
    val notificationIds: List<String>
)

@Serializable
data class BulkMarkNotificationsAsReadResponseDto(
    val message: String? = null
)
