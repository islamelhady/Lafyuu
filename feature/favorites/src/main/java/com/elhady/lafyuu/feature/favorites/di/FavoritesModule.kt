package com.elhady.lafyuu.feature.favorites.di

import com.elhady.lafyuu.feature.favorites.data.repository.FavoriteProductsRepositoryImpl
import com.elhady.lafyuu.feature.favorites.domain.repository.FavoriteProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesModule {
    @Binds
    abstract fun bindFavoriteProductsRepository(
        impl: FavoriteProductsRepositoryImpl
    ): FavoriteProductsRepository
}