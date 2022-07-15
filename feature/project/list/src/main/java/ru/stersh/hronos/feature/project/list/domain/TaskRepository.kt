package ru.stersh.hronos.feature.project.list.domain

import kotlinx.coroutines.flow.Flow

internal interface TaskRepository {
    fun hasRunningTasks(): Flow<Boolean>
    suspend fun stopTask(projectId: Long)
    suspend fun stopRunningTasks()
    suspend fun stopRunningAndStartNewTask(projectId: Long)
}