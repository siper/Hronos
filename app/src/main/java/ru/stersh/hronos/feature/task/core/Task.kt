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
    /**
     * Возвращает законченую задачу
     */
    fun end(): Task {
        return this.copy(endedAt = System.currentTimeMillis())
    }

    companion object {
        const val TASK_TABLE = "tasks"

        /**
         * Создание новой задачи с теущим временем
         * @param projectId - id проекта
         * @param title - заголовок задачи
         */
        fun start(projectId: Long, title: String = ""): Task {
            return Task(
                projectId = projectId,
                title = title,
                startedAt = System.currentTimeMillis(),
                endedAt = 0L
            )
        }
    }
}