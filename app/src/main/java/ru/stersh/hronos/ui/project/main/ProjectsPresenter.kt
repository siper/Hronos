package ru.stersh.hronos.ui.project.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import ru.stersh.hronos.feature.category.CategoryInteractor
import ru.stersh.hronos.feature.project.ProjectInteractor
import ru.stersh.hronos.feature.task.TaskInteractor

class ProjectsPresenter(
    private val projectInteractor: ProjectInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val tasksInteractor: TaskInteractor
) : MvpPresenter<ProjectsView>() {

    override fun onFirstViewAttach() {
        projectInteractor
            .getProjects()
            .combine(categoryInteractor.getCategories()) { projects, categories ->
                return@combine categories
                    .map { category ->
                        UiProjectSection(
                            category,
                            projects
                                .filter { it.categoryId == category.id }
                                .sortedBy { it.order }
                                .toMutableList()
                        )
                    }
                    .sortedBy { it.projects.count() }
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                if (it.isEmpty()) {
                    viewState.showEmptyView()
                } else {
                    viewState.updateSections(it)
                }
            }
            .launchIn(presenterScope)
        tasksInteractor
            .hasRunningTasks()
            .flowOn(Dispatchers.IO)
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
        tasksInteractor.stopRunningTasks()
    }

    fun onStartStopClick(project: UiProject) = presenterScope.launch {
        if (project.isRunning) {
            tasksInteractor.stopTask(project.id)
        } else {
            tasksInteractor.stopRunningAndStartNewTask(project.id)
        }
    }
}