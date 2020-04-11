package ru.stersh.hronos.feature.task.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.feature.task.core.Task.Companion.TASK_TABLE

@Entity(tableName = TASK_TABLE)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val projectId: Long,
    val startedAt: Long,
    val endedAt: Long
) {
    companion object {
        const val TASK_TABLE = "tasks"
    }
}