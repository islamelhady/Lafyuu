package com.elhady.lafyuu.feature.product.di

import com.elhady.lafyuu.feature.product.data.remote.ProductApi
import com.elhady.lafyuu.feature.product.data.repository.ProductRepositoryImpl
import com.elhady.lafyuu.feature.product.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    companion object {
        @Provides
        @Singleton
        fun provideProductApi(retrofit: Retrofit): ProductApi {
            return retrofit.create(ProductApi::class.java)
        }
    }
}
