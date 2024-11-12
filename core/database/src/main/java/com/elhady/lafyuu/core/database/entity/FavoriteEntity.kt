package com.elhady.lafyuu.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val imageUrl: String?,
    val price: Double,
    val originalPrice: Double?,
    val discountPercentage: Int,
    val rating: Int,
    val addedAt: Long
)
