package com.elhady.lafyuu.feature.notifications.domain.model

data class Notification(
    val id: String,
    val notificationText: String,
    val createdAt: String?,
    val isRead: Boolean
)
