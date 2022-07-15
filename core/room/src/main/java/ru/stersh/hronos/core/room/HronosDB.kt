package ru.stersh.hronos.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stersh.hronos.core.room.category.CategoryDao
import ru.stersh.hronos.core.room.category.CategoryDb
import ru.stersh.hronos.core.room.project.ProjectDao
import ru.stersh.hronos.core.room.project.ProjectDb
import ru.stersh.hronos.core.room.task.TaskDao
import ru.stersh.hronos.core.room.task.TaskDb

@Database(entities = [ProjectDb::class, TaskDb::class, CategoryDb::class], version = 1, exportSchema = false)
abstract class HronosDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "hronos-database"
    }
}