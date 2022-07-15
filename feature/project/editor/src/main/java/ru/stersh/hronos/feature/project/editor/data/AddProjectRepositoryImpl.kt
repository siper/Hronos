package ru.stersh.hronos.feature.project.editor.data

import kotlinx.coroutines.flow.first
import ru.stersh.hronos.core.room.category.CategoryDao
import ru.stersh.hronos.core.room.category.CategoryDb
import ru.stersh.hronos.core.room.project.ProjectDao
import ru.stersh.hronos.core.room.project.ProjectDb
import ru.stersh.hronos.feature.project.editor.domain.AddProjectRepository

internal class AddProjectRepositoryImpl(
    private val projectDao: ProjectDao,
    private val categoryDao: CategoryDao
) : AddProjectRepository {
    override suspend fun addProject(title: String, color: Int, category: String) {
        val categories = categoryDao.getAll().first()
        val lastOrder = categoryDao.getLastOrder() ?: 1
        val categoryId = if (category.trim().isNotEmpty()) {
            val cat = categories.filter { it.title == category.trim() }
            if (cat.isNotEmpty()) {
                cat.first().id
            } else {
                categoryDao.put(
                    CategoryDb(
                        title = category,
                        order = lastOrder + 1
                    )
                )
            }
        } else {
            CategoryDb.INCOMING_ID
        }
        projectDao.put(
            ProjectDb(
                title = title,
                order = -1,
                color = color,
                categoryId = categoryId
            )
        )
    }
}