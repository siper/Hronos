package ru.stersh.hronos.feature.project.main

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import ru.stersh.hronos.feature.project.core.ProjectsInteractor

class ProjectsPresenter(private val interactor: ProjectsInteractor) : MvpPresenter<ProjectsView>() {

    override fun onFirstViewAttach() {
        interactor
            .getCategories()
            .combine(interactor.getProjects()) { categories, projects ->
                return@combine categories.map { category ->
                    ProjectSection(
                        category,
                        projects.filter { it.categoryId == category.id }
                    ) { project -> onStartStopClick(project) }
                }
            }.onEach {
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

    fun onStartStopClick(project: UiProject) = presenterScope.launch {
        if (project.isRunning) {
            interactor.stopTask(project.id)
        } else {
            interactor.startTask(project.id)
        }
    }

    fun stopRunningTasks() = presenterScope.launch {
        interactor.stopRunningTasks()
    }
}