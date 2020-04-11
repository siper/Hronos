package ru.stersh.hronos.feature.category.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface CategoryDao {
    @Query("SELECT * FROM ${Category.CATEGORIES_TABLE}")
    fun getAll(): Flowable<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(category: Category): Long
}