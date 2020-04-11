package ru.stersh.hronos.core.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.core.entity.db.Task.Companion.TASK_TABLE

@Entity(tableName = TASK_TABLE)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val projectId: Int,
    val startedAt: Long,
    val endedAt: Long
) {
    companion object {
        const val TASK_TABLE = "tasks"
    }
}