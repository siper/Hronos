package ru.stersh.hronos.feature.task.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM ${Task.TASK_TABLE}")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE projectId = :projectId")
    suspend fun getByProjectId(projectId: Long): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(vararg task: Task)
}