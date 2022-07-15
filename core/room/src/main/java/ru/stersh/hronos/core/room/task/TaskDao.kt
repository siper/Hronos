package ru.stersh.hronos.core.room.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM ${TaskDb.TASK_TABLE}")
    fun getAll(): Flow<List<TaskDb>>

    @Query("SELECT COUNT(id) FROM ${TaskDb.TASK_TABLE} WHERE endedAt = 0")
    fun runningCount(): Flow<Int>

    @Query("SELECT * FROM ${TaskDb.TASK_TABLE} WHERE endedAt = 0")
    suspend fun getRunning(): List<TaskDb>

    @Query("SELECT * FROM ${TaskDb.TASK_TABLE} WHERE endedAt = 0 AND projectId = :projectId")
    suspend fun getRunning(projectId: Long): List<TaskDb>

    @Query("SELECT * FROM ${TaskDb.TASK_TABLE} WHERE projectId = :projectId")
    suspend fun getByProjectId(projectId: Long): List<TaskDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(vararg task: TaskDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(tasks: List<TaskDb>)
}