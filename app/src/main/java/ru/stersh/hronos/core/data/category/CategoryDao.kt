package ru.stersh.hronos.core.data.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${Category.CATEGORIES_TABLE}")
    fun getAll(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(category: Category): Long

    @Query("SELECT `order` FROM ${Category.CATEGORIES_TABLE} ORDER BY `order` DESC LIMIT 1")
    suspend fun getLastOrder(): Int?
}