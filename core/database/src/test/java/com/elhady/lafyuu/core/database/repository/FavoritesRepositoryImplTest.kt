package com.elhady.lafyuu.core.database.repository

import com.elhady.lafyuu.core.database.dao.FavoriteDao
import com.elhady.lafyuu.core.database.entity.FavoriteEntity
import com.elhady.lafyuu.core.database.model.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesRepositoryImplTest {

    private class FakeFavoriteDao : FavoriteDao {
        private val store = MutableStateFlow<Map<String, FavoriteEntity>>(emptyMap())

        override fun observeFavorites(): Flow<List<FavoriteEntity>> =
            store.map { it.values.sortedByDescending { entity -> entity.addedAt } }

        override fun isFavorite(productId: String): Flow<Boolean> =
            store.map { productId in it }

        override suspend fun insertFavorite(entity: FavoriteEntity) {
            store.update { it + (entity.productId to entity) }
        }

        override suspend fun deleteFavorite(productId: String) {
            store.update { it - productId }
        }

        override suspend fun clearFavorites() {
            store.value = emptyMap()
        }
    }

    private val repository = FavoritesRepositoryImpl(FakeFavoriteDao())

    private fun item(id: String) = Favorite(
        productId = id,
        name = "Shoe $id",
        imageUrl = "https://example.com/$id.jpg",
        price = 80.0,
        originalPrice = 100.0,
        discountPercentage = 20,
        rating = 4
    )

    @Test
    fun `added favorite is stored with its snapshot`() = runBlocking {
        repository.addFavorite(item("p-1"))

        assertEquals(listOf(item("p-1")), repository.observeFavorites().first())
        assertTrue(repository.isFavorite("p-1").first())
    }

    @Test
    fun `removed favorite disappears`() = runBlocking {
        repository.addFavorite(item("p-1"))

        repository.removeFavorite("p-1")

        assertTrue(repository.observeFavorites().first().isEmpty())
        assertFalse(repository.isFavorite("p-1").first())
    }

    @Test
    fun `clear removes everything`() = runBlocking {
        repository.addFavorite(item("p-1"))
        repository.addFavorite(item("p-2"))

        repository.clearFavorites()

        assertTrue(repository.observeFavorites().first().isEmpty())
    }

    @Test
    fun `blank product id is ignored`() = runBlocking {
        repository.addFavorite(item(" "))

        assertTrue(repository.observeFavorites().first().isEmpty())
    }
}