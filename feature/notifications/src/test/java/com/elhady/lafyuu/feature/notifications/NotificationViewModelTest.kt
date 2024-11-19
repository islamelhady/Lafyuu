package com.elhady.lafyuu.feature.notifications

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.core.common.result.AppError
import com.elhady.lafyuu.core.designsystem.components.element.AlertType
import com.elhady.lafyuu.feature.notifications.domain.model.Notification
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage
import com.elhady.lafyuu.feature.notifications.domain.usecase.GetNotificationsUseCase
import com.elhady.lafyuu.feature.notifications.domain.usecase.MarkNotificationAsReadUseCase
import com.elhady.lafyuu.feature.notifications.presentation.NotificationUiEffect
import com.elhady.lafyuu.feature.notifications.presentation.NotificationUiIntent
import com.elhady.lafyuu.feature.notifications.presentation.NotificationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationViewModelTest {

    private val getNotificationsUseCase: GetNotificationsUseCase = mockk()
    private val markAsReadUseCase: MarkNotificationAsReadUseCase = mockk()

    private fun notification(id: String, isRead: Boolean = false) =
        Notification(id = id, text = "Text $id", createdAt = null, isRead = isRead)

    private fun page(vararg items: Notification, hasNext: Boolean = false, number: Int = 1) =
        AppResult.Success(NotificationsPage(items.toList(), number, hasNext))

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { getNotificationsUseCase(any()) } returns page(notification("n1"), notification("n2", isRead = true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        NotificationViewModel(getNotificationsUseCase, markAsReadUseCase)

    @Test
    fun `loads the first page on init`() = runTest {
        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertEquals(listOf("n1", "n2"), state.notifications.map { it.id })
        assertNull(state.error)
    }

    @Test
    fun `failed first load shows an error and retry loads again`() = runTest {
        coEvery { getNotificationsUseCase(any()) } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        assertNotNull(viewModel.uiState.value.error)

        coEvery { getNotificationsUseCase(any()) } returns page(notification("n1"))
        viewModel.onIntent(NotificationUiIntent.RetryClicked)

        assertNull(viewModel.uiState.value.error)
        assertEquals(1, viewModel.uiState.value.notifications.size)
    }

    @Test
    fun `clicking an unread notification marks it as read`() = runTest {
        coEvery { markAsReadUseCase("n1") } returns AppResult.Success(Unit)
        val viewModel = createViewModel()

        viewModel.onIntent(NotificationUiIntent.NotificationClicked("n1"))

        assertTrue(viewModel.uiState.value.notifications.first { it.id == "n1" }.isRead)
        coVerify(exactly = 1) { markAsReadUseCase("n1") }
    }

    @Test
    fun `clicking a read notification does not call the server`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(NotificationUiIntent.NotificationClicked("n2"))

        coVerify(exactly = 0) { markAsReadUseCase(any()) }
    }

    @Test
    fun `mark as read failure keeps it unread and shows a snackbar`() = runTest {
        coEvery { markAsReadUseCase("n1") } returns AppResult.Failure(AppError.NoInternet)
        val viewModel = createViewModel()
        val effects = mutableListOf<NotificationUiEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEffect.toList(effects)
        }

        viewModel.onIntent(NotificationUiIntent.NotificationClicked("n1"))

        assertFalse(viewModel.uiState.value.notifications.first { it.id == "n1" }.isRead)
        val effect = effects.single() as NotificationUiEffect.ShowSnackbar
        assertEquals(AlertType.Error, effect.type)
    }

    @Test
    fun `load more appends the next page without duplicates`() = runTest {
        coEvery { getNotificationsUseCase(1) } returns
                page(notification("n1"), notification("n2"), hasNext = true)
        coEvery { getNotificationsUseCase(2) } returns
                page(notification("n2"), notification("n3"), number = 2)
        val viewModel = createViewModel()

        viewModel.onIntent(NotificationUiIntent.LoadMore)

        val state = viewModel.uiState.value
        assertEquals(listOf("n1", "n2", "n3"), state.notifications.map { it.id })
        assertFalse(state.hasNextPage)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `load more is ignored when there is no next page`() = runTest {
        val viewModel = createViewModel()

        viewModel.onIntent(NotificationUiIntent.LoadMore)

        coVerify(exactly = 1) { getNotificationsUseCase(any()) }
    }
}