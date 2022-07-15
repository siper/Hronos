package ru.stersh.hronos.core.room.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${CategoryDb.CATEGORIES_TABLE}")
    fun getAll(): Flow<List<CategoryDb>>

    @Query("SELECT title FROM ${CategoryDb.CATEGORIES_TABLE} WHERE title LIKE '%' || :query || '%'")
    suspend fun getCategoryTitles(query: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(category: CategoryDb): Long

    @Query("SELECT `order` FROM ${CategoryDb.CATEGORIES_TABLE} ORDER BY `order` DESC LIMIT 1")
    suspend fun getLastOrder(): Int?
}