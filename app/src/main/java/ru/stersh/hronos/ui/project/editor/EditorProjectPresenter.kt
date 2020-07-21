package ru.stersh.hronos.ui.project.editor


import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import ru.stersh.hronos.feature.category.CategoryInteractor
import ru.stersh.hronos.feature.project.ProjectInteractor

class EditorProjectPresenter(
    private val projectInteractor: ProjectInteractor,
    private val categoryInteractor: CategoryInteractor
) : MvpPresenter<EditorProjectView>() {

    fun addProject(title: String, color: Int, category: String) {
        if (title.isEmpty() || title.isBlank()) {
            viewState.showError()
            return
        }
        presenterScope.launch {
            projectInteractor.addProject(title, color, category)
            viewState.done()
        }
    }

    fun requestSuggestions(query: String) {
        if (query.isEmpty() || query.isBlank()) return
        presenterScope.launch {
            val categories = categoryInteractor.getCategories().first()
            viewState.fillSuggestions(
                categories
                    .map { it.title }
                    .filter { it.contains(query, ignoreCase = true) }
            )
        }
    }
}