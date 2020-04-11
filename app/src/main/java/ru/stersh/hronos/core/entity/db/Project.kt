package ru.stersh.hronos.core.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.core.entity.db.Project.Companion.PROJECTS_TABLE

@Entity(tableName = PROJECTS_TABLE)
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val order: Int,
    val color: Int
) {
    companion object {
        const val PROJECTS_TABLE = "projects"
    }
}