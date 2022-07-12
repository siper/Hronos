package ru.stersh.hronos.core.data.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.core.data.category.Category.Companion.CATEGORIES_TABLE

@Entity(tableName = CATEGORIES_TABLE)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val order: Int
) {
    companion object {
        const val CATEGORIES_TABLE = "categories"

        const val FAVORITE_ID = 1L
        const val INCOMING_ID = 2L
    }
}