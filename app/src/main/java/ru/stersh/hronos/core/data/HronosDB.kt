package ru.stersh.hronos.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stersh.hronos.core.data.category.Category
import ru.stersh.hronos.core.data.category.CategoryDao
import ru.stersh.hronos.core.data.project.Project
import ru.stersh.hronos.core.data.project.ProjectDao
import ru.stersh.hronos.core.data.task.Task
import ru.stersh.hronos.core.data.task.TaskDao

@Database(entities = [Project::class, Task::class, Category::class], version = 1, exportSchema = false)
abstract class HronosDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "hronos-database"
    }
}