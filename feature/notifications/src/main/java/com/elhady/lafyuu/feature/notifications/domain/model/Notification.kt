package com.elhady.lafyuu.feature.notifications.domain.model

import java.time.Instant
data class Notification(
    val id: String,
    val text: String,
    val createdAt: Instant?,
    val isRead: Boolean
)

data class NotificationsPage(
    val items: List<Notification>,
    val page: Int,
    val hasNextPage: Boolean
)