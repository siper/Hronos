package ru.stersh.hronos.feature.project.editor.data

import ru.stersh.hronos.core.room.category.CategoryDao
import ru.stersh.hronos.feature.project.editor.domain.CategorySuggestionsRepository

internal class CategorySuggestionsRepositoryImpl(private val categoryDao: CategoryDao) : CategorySuggestionsRepository {
    override suspend fun getCategorySuggestions(query: String): List<String> {
        return categoryDao
            .getCategoryTitles(query)
    }
}