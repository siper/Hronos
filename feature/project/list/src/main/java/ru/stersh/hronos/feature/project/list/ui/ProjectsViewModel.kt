package ru.stersh.hronos.feature.project.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.stersh.hronos.core.utils.extention.mapItems
import ru.stersh.hronos.feature.project.list.domain.SectionsRepository
import ru.stersh.hronos.feature.project.list.domain.TaskRepository
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.ProjectUi
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.SectionUi

internal class ProjectsViewModel(
    private val sectionsRepository: SectionsRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<SectionUi>>(emptyList())
    val sections: Flow<List<SectionUi>>
        get() = _sections

    private val _mainButtonState = MutableStateFlow<MainButtonStateUi>(MainButtonStateUi.ADD_PROJECT)
    val mainButtonState: Flow<MainButtonStateUi>
        get() = _mainButtonState

    init {
        subscribeSections()
        subscribeMainButtonState()
    }

    fun stopRunningTasks() = viewModelScope.launch {
        taskRepository.stopRunningTasks()
    }

    fun onStartStopClick(project: ProjectUi) = viewModelScope.launch {
        if (project.isRunning) {
            taskRepository.stopTask(project.id)
        } else {
            taskRepository.stopRunningAndStartNewTask(project.id)
        }
    }

    private fun subscribeSections() = viewModelScope.launch {
        sectionsRepository
            .getSections()
            .mapItems { it.toUi() }
            .collect {
                _sections.value = it
            }
    }

    private fun subscribeMainButtonState() = viewModelScope.launch {
        taskRepository
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