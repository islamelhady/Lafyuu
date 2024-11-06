package com.elhady.lafyuu.feature.orders.di

import com.elhady.lafyuu.core.network.error.SafeApiCaller
import com.elhady.lafyuu.feature.orders.data.remote.OrdersApi
import com.elhady.lafyuu.feature.orders.data.repository.OrdersRepositoryImpl
import com.elhady.lafyuu.feature.orders.domain.repository.OrdersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {

    @Provides
    @Singleton
    fun provideOrdersApi(retrofit: Retrofit): OrdersApi {
        return retrofit.create(OrdersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrdersRepository(
        ordersApi: OrdersApi,
        safeApiCaller: SafeApiCaller
    ): OrdersRepository {
        return OrdersRepositoryImpl(ordersApi, safeApiCaller)
    }
}
