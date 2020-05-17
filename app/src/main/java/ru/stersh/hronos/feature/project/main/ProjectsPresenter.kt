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
            .getCategories()
            .combine(interactor.getProjects()) { categories, projects ->
                return@combine Pair(categories, projects)
            }.onEach {
                if (it.second.isEmpty()) {
                    viewState.showEmptyView()
                    viewState.showAddProjectButton()
                    return@onEach
                }
                viewState.updateProjects(it.second, it.first)
                val hasRunningTask = it.second.filter { it.isRunning }.isNotEmpty()
                if (hasRunningTask) {
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
            interactor.stopRunningTasks()
            interactor.startTask(project.id)
        }
    }


    fun stopRunningTasks() = presenterScope.launch {
        interactor.stopRunningTasks()
    }
}