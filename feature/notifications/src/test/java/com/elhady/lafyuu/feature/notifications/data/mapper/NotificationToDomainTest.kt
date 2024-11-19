package com.elhady.lafyuu.feature.notifications.data.mapper

import com.elhady.lafyuu.feature.notifications.data.remote.model.NotificationDto
import com.elhady.lafyuu.feature.notifications.data.remote.model.PagedListOfNotificationDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class NotificationToDomainTest {

    private val expected = Instant.parse("2014-04-30T13:01:00Z")

    @Test
    fun `date with offset is parsed`() {
        val dto = NotificationDto(id = "n1", notificationText = "Hi", createdAt = "2014-04-30T15:01:00+02:00")

        assertEquals(expected, dto.toDomain().createdAt)
    }

    @Test
    fun `date without offset is treated as UTC and fractional seconds are supported`() {
        val dto = NotificationDto(id = "n1", createdAt = "2014-04-30T13:01:00.1234567")

        assertEquals(expected.plusNanos(123_456_700), dto.toDomain().createdAt)
    }

    @Test
    fun `unknown date format gives null instead of crashing`() {
        assertNull(NotificationDto(id = "n1", createdAt = "yesterday").toDomain().createdAt)
    }

    @Test
    fun `missing fields get safe defaults`() {
        val notification = NotificationDto(id = "n1").toDomain()

        assertEquals("", notification.text)
        assertEquals(false, notification.isRead)
    }

    @Test
    fun `page mapping drops items without id and keeps hasNextPage`() {
        val dto = PagedListOfNotificationDto(
            items = listOf(NotificationDto(id = "n1"), NotificationDto(id = null), NotificationDto(id = " ")),
            hasNextPage = true
        )

        val page = dto.toDomain(requestedPage = 3)

        assertEquals(listOf("n1"), page.items.map { it.id })
        assertEquals(3, page.page)
        assertTrue(page.hasNextPage)
    }
}