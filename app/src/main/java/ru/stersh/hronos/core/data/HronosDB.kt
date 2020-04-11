package ru.stersh.hronos.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stersh.hronos.core.entity.db.Project
import ru.stersh.hronos.core.entity.db.Task
import ru.stersh.hronos.feature.project.core.ProjectDao
import ru.stersh.hronos.task.model.repository.TaskDao

@Database(entities = [Project::class, Task::class], version = 1)
abstract class HronosDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
}