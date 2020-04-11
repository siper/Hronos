package ru.stersh.hronos.feature.project.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.feature.project.core.Project.Companion.PROJECTS_TABLE

@Entity(tableName = PROJECTS_TABLE)
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val order: Int,
    val color: Int,
    val categoryId: Long = -1
) {
    companion object {
        const val PROJECTS_TABLE = "projects"
    }
}