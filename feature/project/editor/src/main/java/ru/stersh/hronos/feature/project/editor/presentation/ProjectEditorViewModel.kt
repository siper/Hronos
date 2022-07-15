package ru.stersh.hronos.feature.project.editor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.stersh.hronos.feature.project.editor.domain.AddProjectRepository
import ru.stersh.hronos.feature.project.editor.domain.CategorySuggestionsRepository

internal class ProjectEditorViewModel(
    private val addProjectRepository: AddProjectRepository,
    private val categorySuggestionsRepository: CategorySuggestionsRepository
) : ViewModel() {

    private val _error = MutableStateFlow(false)
    val error: Flow<Boolean>
        get() = _error

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: Flow<List<String>>
        get() = _suggestions

    private val _exit = Channel<Unit>()
    val exit: Flow<Unit>
        get() = _exit.receiveAsFlow()

    fun addProject(title: String, color: Int, category: String) {
        if (title.isEmpty() || title.isBlank()) {
            _error.value = true
            return
        } else {
            _error.value = false
        }
        viewModelScope.launch {
            addProjectRepository.addProject(title, color, category)
            _exit.send(Unit)
        }
    }

    fun requestSuggestions(query: String) {
        if (query.isEmpty() || query.isBlank()) {
            return
        }
        viewModelScope.launch {
            _suggestions.value = categorySuggestionsRepository.getCategorySuggestions(query)
        }
    }
}