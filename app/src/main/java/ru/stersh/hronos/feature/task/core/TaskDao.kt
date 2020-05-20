package ru.stersh.hronos.feature.task.core

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {
    @Query("SELECT * FROM ${Task.TASK_TABLE}")
    abstract fun getAll(): Flow<List<Task>>

    @Query("SELECT COUNT(id) FROM ${Task.TASK_TABLE} WHERE endedAt = 0")
    abstract fun runningCount(): Flow<Int>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE endedAt = 0")
    abstract suspend fun getRunning(): List<Task>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE endedAt = 0 AND projectId = :projectId")
    abstract suspend fun getRunning(projectId: Long): List<Task>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE projectId = :projectId")
    abstract suspend fun getByProjectId(projectId: Long): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun put(vararg task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun put(tasks: List<Task>)

    @Transaction
    suspend open fun startTask(projectId: Long) {
        val tasks = getRunning()
            .map { it.end() }
        put(tasks)
        put(Task.start(projectId))
    }
}