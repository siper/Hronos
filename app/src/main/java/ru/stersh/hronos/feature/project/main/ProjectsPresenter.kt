package ru.stersh.hronos.feature.project.main

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import ru.stersh.hronos.feature.project.core.ProjectsInteractor

class ProjectsPresenter(private val interactor: ProjectsInteractor) : MvpPresenter<ProjectsView>() {

    override fun onFirstViewAttach() {
        interactor
            .getProjects()
            .combine(interactor.getCategories()) { projects, categories ->
                return@combine categories
                    .map { category ->
                        ProjectSection(
                            category,
                            projects.filter { it.categoryId == category.id },
                            ::onStartStopClick
                        )
                    }
                    .filter { it.data.isNotEmpty() }
            }
            .onEach {
                if (it.isEmpty()) {
                    viewState.showEmptyView()
                } else {
                    viewState.updateSections(it)
                }
            }
            .launchIn(presenterScope)
        interactor
            .hasRunningTasks()
            .onEach {
                if (it) {
                    viewState.showStopTaskButton()
                } else {
                    viewState.showAddProjectButton()
                }
            }
            .launchIn(presenterScope)
    }

    fun stopRunningTasks() = presenterScope.launch {
        interactor.stopRunningTasks()
    }

    private fun onStartStopClick(project: UiProject) {
        presenterScope.launch {
            if (project.isRunning) {
                interactor.stopTask(project.id)
            } else {
                interactor.stopRuningAndStartTask(project.id)
            }
        }
    }
}