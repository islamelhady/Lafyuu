package com.elhady.lafyuu.feature.product.presentation.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneOffset
import java.util.Locale

class ProductToUiTest {

    @Test
    fun `date with offset is formatted as a long date`() {
        assertEquals(
            "December 10, 2016",
            formatReviewDate("2016-12-10T09:30:00Z", Locale.US, ZoneOffset.UTC)
        )
    }

    @Test
    fun `date without offset is treated as UTC and converted to the device zone`() {
        assertEquals(
            "December 11, 2016",
            formatReviewDate("2016-12-10T23:30:00", Locale.US, ZoneOffset.ofHours(2))
        )
    }

    @Test
    fun `fractional seconds from the backend are supported`() {
        assertEquals(
            "December 10, 2016",
            formatReviewDate("2016-12-10T09:30:00.1234567Z", Locale.US, ZoneOffset.UTC)
        )
    }

    @Test
    fun `date only is not shifted by timezone`() {
        assertEquals(
            "December 10, 2016",
            formatReviewDate("2016-12-10", Locale.US, ZoneOffset.ofHours(-8))
        )
    }

    @Test
    fun `unknown format falls back to the raw string and blank stays blank`() {
        assertEquals("yesterday", formatReviewDate("yesterday", Locale.US, ZoneOffset.UTC))
        assertEquals("", formatReviewDate("", Locale.US, ZoneOffset.UTC))
    }
}