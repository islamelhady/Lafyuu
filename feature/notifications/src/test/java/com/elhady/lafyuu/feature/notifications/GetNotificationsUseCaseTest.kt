package com.elhady.lafyuu.feature.notifications

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import com.elhady.lafyuu.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.elhady.lafyuu.feature.notifications.domain.usecase.MarkNotificationAsReadUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationUseCasesTest {

    private val repository: NotificationRepository = mockk()

    @Test
    fun `get notifications uses the page size`() = runTest {
        val page = NotificationsPage(emptyList(), 2, false)
        coEvery { repository.getNotifications(2, 20) } returns AppResult.Success(page)

        assertEquals(AppResult.Success(page), GetNotificationsUseCase(repository)(page = 2))
    }

    @Test
    fun `mark as read rejects a blank id without calling the repository`() = runTest {
        val result = MarkNotificationAsReadUseCase(repository)(" ")

        assertEquals(AppResult.Failure(AppError.Validation("Invalid notification ID")), result)
        coVerify(exactly = 0) { repository.markAsRead(any()) }
    }

    @Test
    fun `mark as read delegates to the repository`() = runTest {
        coEvery { repository.markAsRead("n1") } returns AppResult.Success(Unit)

        assertEquals(AppResult.Success(Unit), MarkNotificationAsReadUseCase(repository)("n1"))
    }
}