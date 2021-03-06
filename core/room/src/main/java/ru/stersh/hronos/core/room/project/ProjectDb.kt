package ru.stersh.hronos.core.room.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stersh.hronos.core.room.project.ProjectDb.Companion.PROJECTS_TABLE

@Entity(tableName = PROJECTS_TABLE)
data class ProjectDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "order")
    val order: Int,
    @ColumnInfo(name = "color")
    val color: Int,
    @ColumnInfo(name = "categoryId")
    val categoryId: Long = -1
) {
    companion object {
        const val PROJECTS_TABLE = "projects"
    }
}