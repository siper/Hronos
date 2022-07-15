package ru.stersh.hronos.feature.project.editor.domain

internal interface AddProjectRepository {
    suspend fun addProject(title: String, color: Int, category: String)
}