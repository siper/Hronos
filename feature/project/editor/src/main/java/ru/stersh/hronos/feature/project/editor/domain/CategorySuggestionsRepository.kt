package ru.stersh.hronos.feature.project.editor.domain

internal interface CategorySuggestionsRepository {
    suspend fun getCategorySuggestions(query: String): List<String>
}