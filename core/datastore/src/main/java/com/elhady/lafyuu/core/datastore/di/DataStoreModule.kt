package com.elhady.lafyuu.core.datastore.di

import com.elhady.lafyuu.core.datastore.DataStoreTokenProvider
import com.elhady.lafyuu.core.network.auth.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindTokenProvider(dataStoreTokenProvider: DataStoreTokenProvider): TokenProvider
}
