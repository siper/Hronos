package ru.stersh.hronos.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stersh.hronos.feature.category.core.Category
import ru.stersh.hronos.feature.category.core.CategoryDao
import ru.stersh.hronos.feature.project.core.Project
import ru.stersh.hronos.feature.project.core.ProjectDao
import ru.stersh.hronos.feature.task.core.Task
import ru.stersh.hronos.feature.task.core.TaskDao

@Database(entities = [Project::class, Task::class, Category::class], version = 3)
abstract class HronosDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
}