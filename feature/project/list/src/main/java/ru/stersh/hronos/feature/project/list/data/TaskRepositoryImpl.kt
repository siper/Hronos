package ru.stersh.hronos.feature.project.list.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.hronos.core.room.task.TaskDao
import ru.stersh.hronos.core.room.task.TaskDb
import ru.stersh.hronos.feature.project.list.domain.TaskRepository

internal class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override fun hasRunningTasks(): Flow<Boolean> {
        return taskDao.runningCount().map { it > 0 }
    }

    override suspend fun stopTask(projectId: Long) {
        val tasks = taskDao
            .getRunning(projectId)
            .map { it.end() }
        taskDao.put(tasks)
    }

    override suspend fun stopRunningTasks() {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
        taskDao.put(tasks)
    }

    override suspend fun stopRunningAndStartNewTask(projectId: Long) {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
            .toMutableList()
            .apply { add(TaskDb.start(projectId)) }
        taskDao.put(tasks)
    }
}