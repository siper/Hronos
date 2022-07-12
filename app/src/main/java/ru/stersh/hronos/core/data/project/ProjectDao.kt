package ru.stersh.hronos.core.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ${Project.PROJECTS_TABLE}")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(vararg projects: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(projects: List<Project>)

    @Query("SELECT * FROM ${Project.PROJECTS_TABLE} WHERE id = :id LIMIT 1")
    suspend fun getProject(id: Long): Project?
}