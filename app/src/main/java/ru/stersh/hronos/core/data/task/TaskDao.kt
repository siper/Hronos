package ru.stersh.hronos.core.data.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM ${Task.TASK_TABLE}")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT COUNT(id) FROM ${Task.TASK_TABLE} WHERE endedAt = 0")
    fun runningCount(): Flow<Int>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE endedAt = 0")
    suspend fun getRunning(): List<Task>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE endedAt = 0 AND projectId = :projectId")
    suspend fun getRunning(projectId: Long): List<Task>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE projectId = :projectId")
    suspend fun getByProjectId(projectId: Long): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(vararg task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(tasks: List<Task>)
}