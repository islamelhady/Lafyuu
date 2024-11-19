package com.elhady.lafyuu.feature.notifications.data.mapper

import com.elhady.lafyuu.feature.notifications.data.remote.model.NotificationDto
import com.elhady.lafyuu.feature.notifications.data.remote.model.PagedListOfNotificationDto
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun NotificationDto.toDomain() = Notification(
    id = id.orEmpty(),
    text = notificationText.orEmpty(),
    createdAt = createdAt?.let(::parseInstantOrNull),
    isRead = isRead ?: false
)

fun PagedListOfNotificationDto.toDomain(requestedPage: Int) = NotificationsPage(
    items = items.orEmpty().filter { !it.id.isNullOrBlank() }.map { it.toDomain() },
    page = page ?: requestedPage,
    hasNextPage = hasNextPage ?: false
)

private fun parseInstantOrNull(raw: String): Instant? =
    runCatching { OffsetDateTime.parse(raw).toInstant() }.getOrNull()
        ?: runCatching { LocalDateTime.parse(raw).toInstant(ZoneOffset.UTC) }.getOrNull()