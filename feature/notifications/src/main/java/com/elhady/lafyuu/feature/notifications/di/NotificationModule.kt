package com.elhady.lafyuu.feature.notifications.di

import com.elhady.lafyuu.core.network.error.ApiErrorParser
import com.elhady.lafyuu.feature.notifications.data.remote.NotificationApi
import com.elhady.lafyuu.feature.notifications.data.repository.NotificationRepositoryImpl
import com.elhady.lafyuu.feature.notifications.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationApi: NotificationApi,
        apiErrorParser: ApiErrorParser
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationApi, apiErrorParser)
    }
}
