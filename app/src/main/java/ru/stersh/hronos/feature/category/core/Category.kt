package ru.stersh.hronos.feature.category.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.feature.category.core.Category.Companion.CATEGORIES_TABLE

@Entity(tableName = CATEGORIES_TABLE)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String
) {
    companion object {
        const val CATEGORIES_TABLE = "categories"

        const val INCOMING_ID = 1L
        const val FAVORITE_ID = 2L
    }
}