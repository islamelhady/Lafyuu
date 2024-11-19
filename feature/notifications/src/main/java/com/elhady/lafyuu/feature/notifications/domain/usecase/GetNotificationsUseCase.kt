package com.elhady.lafyuu.feature.notifications.domain.usecase

import com.elhady.lafyuu.core.common.AppResult
import com.elhady.lafyuu.feature.notifications.domain.model.NotificationsPage
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(page: Int = FIRST_PAGE): AppResult<NotificationsPage> =
        repository.getNotifications(page, PAGE_SIZE)

    companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 20
    }
}