package com.elhady.lafyuu.feature.notifications.data.remote

import com.elhady.lafyuu.feature.notifications.data.remote.model.BulkMarkNotificationsAsReadRequestDto
import com.elhady.lafyuu.feature.notifications.data.remote.model.BulkMarkNotificationsAsReadResponseDto
import com.elhady.lafyuu.feature.notifications.data.remote.model.GetUserNotificationsResponseDto
import com.elhady.lafyuu.feature.notifications.data.remote.model.MarkNotificationAsReadResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(
        @Query("includeRead") includeRead: Boolean? = true,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 20
    ): GetUserNotificationsResponseDto

    @POST("api/notifications/{notificationId}/read")
    suspend fun markAsRead(
        @Path("notificationId") notificationId: String
    ): MarkNotificationAsReadResponseDto

    @POST("api/notifications/bulk-read")
    suspend fun bulkMarkAsRead(
        @Body request: BulkMarkNotificationsAsReadRequestDto
    ): BulkMarkNotificationsAsReadResponseDto
}
