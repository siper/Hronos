package ru.stersh.hronos.task.model.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import ru.stersh.hronos.core.entity.db.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM ${Task.TASK_TABLE}")
    fun getAll(): Flowable<List<Task>>

    @Query("SELECT * FROM ${Task.TASK_TABLE} WHERE projectId = :projectId")
    fun getByProjectId(projectId: Int): Flowable<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(task: Task)
}