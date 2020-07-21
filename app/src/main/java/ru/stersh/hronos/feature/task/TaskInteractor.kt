package ru.stersh.hronos.feature.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.hronos.core.data.task.Task
import ru.stersh.hronos.core.data.task.TaskDao

class TaskInteractor(private val taskDao: TaskDao) {

    fun hasRunningTasks(): Flow<Boolean> {
        return taskDao.runningCount().map { it > 0 }
    }

    suspend fun stopTask(projectId: Long) {
        val tasks = taskDao
            .getRunning(projectId)
            .map { it.end() }
        taskDao.put(tasks)
    }

    suspend fun stopRunningTasks() {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
        taskDao.put(tasks)
    }

    suspend fun stopRunningAndStartNewTask(projectId: Long) {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
            .toMutableList()
            .apply { add(Task.start(projectId)) }
        taskDao.put(tasks)
    }
}