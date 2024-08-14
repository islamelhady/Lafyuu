package com.elhady.lafyuu.feature.notifications.data.mapper

import com.elhady.lafyuu.feature.notifications.data.remote.model.NotificationDto
import com.elhady.lafyuu.feature.notifications.domain.model.Notification

fun NotificationDto.toDomain() = Notification(
    id = id.orEmpty(),
    notificationText = notificationText.orEmpty(),
    createdAt = createdAt,
    isRead = isRead ?: false
)
