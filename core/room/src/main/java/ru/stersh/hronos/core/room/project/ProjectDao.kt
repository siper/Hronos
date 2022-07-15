package ru.stersh.hronos.core.room.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ${ProjectDb.PROJECTS_TABLE}")
    fun getAllProjects(): Flow<List<ProjectDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(project: ProjectDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(vararg projects: ProjectDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(projects: List<ProjectDb>)

    @Query("SELECT * FROM ${ProjectDb.PROJECTS_TABLE} WHERE id = :id LIMIT 1")
    suspend fun getProject(id: Long): ProjectDb?
}