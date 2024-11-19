package com.elhady.lafyuu.feature.notifications.data.mapper

import com.elhady.lafyuu.feature.notifications.presentation.mapper.formatNotificationDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import java.util.Locale

class NotificationUiMappersTest {

    @Test
    fun `date and time are formatted for the given locale and zone`() {
        val instant = Instant.parse("2014-04-30T13:01:00Z")

        assertEquals(
            "30 April 2014 15:01",
            formatNotificationDate(instant, Locale.UK, ZoneOffset.ofHours(2))
        )
    }
}