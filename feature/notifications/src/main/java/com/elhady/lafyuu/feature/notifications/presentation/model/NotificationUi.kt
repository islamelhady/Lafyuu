package com.elhady.lafyuu.feature.notifications.presentation.model

data class NotificationUi(
    val id: String,
    val text: String,
    val date: String,
    val isRead: Boolean
)