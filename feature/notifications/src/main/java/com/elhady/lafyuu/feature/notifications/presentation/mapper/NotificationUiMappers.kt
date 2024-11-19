package com.elhady.lafyuu.feature.notifications.presentation.mapper

import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.presentation.model.NotificationUi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Notification.toUi() = NotificationUi(
    id = id,
    text = text,
    date = createdAt?.let { formatNotificationDate(it) }.orEmpty(),
    isRead = isRead
)

internal fun formatNotificationDate(
    instant: Instant,
    locale: Locale = Locale.getDefault(),
    zone: ZoneId = ZoneId.systemDefault()
): String {
    val dateTime = instant.atZone(zone)
    val date = dateTime.toLocalDate()
        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale))
    val time = dateTime.toLocalTime()
        .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale))
    return "$date $time"
}