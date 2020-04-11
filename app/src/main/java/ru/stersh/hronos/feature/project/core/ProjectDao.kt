package ru.stersh.hronos.feature.project.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import ru.stersh.hronos.core.entity.db.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ${Project.PROJECTS_TABLE}")
    fun getAllProjects(): Flowable<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putAll(vararg projects: Project)

    @Query("SELECT * FROM ${Project.PROJECTS_TABLE} WHERE id = :id")
    fun getProject(id: Int): Maybe<Project>
}