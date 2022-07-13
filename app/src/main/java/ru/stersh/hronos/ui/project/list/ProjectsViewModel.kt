package ru.stersh.hronos.ui.project.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.stersh.hronos.feature.category.CategoryInteractor
import ru.stersh.hronos.feature.project.ProjectInteractor
import ru.stersh.hronos.feature.task.TaskInteractor

class ProjectsViewModel(
    private val projectInteractor: ProjectInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val tasksInteractor: TaskInteractor
) : ViewModel() {

    private val _sections = MutableStateFlow<List<UiProjectSection>>(emptyList())
    val sections: Flow<List<UiProjectSection>>
        get() = _sections

    private val _mainButtonState = MutableStateFlow<MainButtonStateUi>(MainButtonStateUi.ADD_PROJECT)
    val mainButtonState: Flow<MainButtonStateUi>
        get() = _mainButtonState

    init {
        subscribeSections()
        subscribeMainButtonState()
    }

    fun stopRunningTasks() = viewModelScope.launch {
        tasksInteractor.stopRunningTasks()
    }

    fun onStartStopClick(project: UiProject) = viewModelScope.launch {
        if (project.isRunning) {
            tasksInteractor.stopTask(project.id)
        } else {
            tasksInteractor.stopRunningAndStartNewTask(project.id)
        }
    }

    private fun subscribeSections() = viewModelScope.launch {
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
            .collect {
                _sections.value = it
            }
    }

    private fun subscribeMainButtonState() = viewModelScope.launch {
        tasksInteractor
            .hasRunningTasks()
            .flowOn(Dispatchers.IO)
            .collect {
                _mainButtonState.value = if (it) {
                    MainButtonStateUi.STOP_TASK
                } else {
                    MainButtonStateUi.ADD_PROJECT
                }
            }
    }

    enum class MainButtonStateUi { ADD_PROJECT, STOP_TASK }
}