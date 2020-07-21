package ru.stersh.hronos.feature.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.hronos.core.data.category.CategoryDao
import ru.stersh.hronos.ui.category.UiCategory

class CategoryInteractor(private val categoryDao: CategoryDao) {

    fun getCategories(): Flow<List<UiCategory>> {
        return categoryDao
            .getAll()
            .map { categories ->
                categories.map { UiCategory(it.id, it.title) }
            }
    }
}