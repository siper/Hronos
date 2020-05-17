package ru.stersh.hronos.feature.project.editor


import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import ru.stersh.hronos.feature.project.core.ProjectsInteractor

class EditorProjectPresenter(
    private val interactor: ProjectsInteractor
) : MvpPresenter<EditorProjectView>() {

    fun addProject(title: String, color: Int, category: String) {
        if (title.isEmpty() || title.isBlank()) {
            viewState.showError()
            return
        }
        presenterScope.launch {
            interactor.addProject(title, color, category)
            viewState.done()
        }
    }

    fun requestSuggestions(query: String) {
        if (query.isEmpty() || query.isBlank()) return
        presenterScope.launch {
            interactor
                .getCategories()
                .collect {
                    viewState.fillSuggestions(
                        it
                            .map { it.title }
                            .filter { it.contains(query, ignoreCase = true) }
                    )
                }
        }
    }
}