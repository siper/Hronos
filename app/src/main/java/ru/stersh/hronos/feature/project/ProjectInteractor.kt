package ru.stersh.hronos.feature.project

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import ru.stersh.hronos.core.data.category.Category
import ru.stersh.hronos.core.data.category.CategoryDao
import ru.stersh.hronos.core.data.project.Project
import ru.stersh.hronos.core.data.project.ProjectDao
import ru.stersh.hronos.core.data.task.TaskDao
import ru.stersh.hronos.ui.project.main.UiProject

class ProjectInteractor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) {

    fun getProjects(): Flow<List<UiProject>> {
        return projectDao.getAllProjects().combine(taskDao.getAll()) { projects, tasks ->
            return@combine projects.map { project ->
                val projectTasks = tasks.filter { it.projectId == project.id }
                var spentTime = 0L
                var isRunning = false
                var startTime = 0L
                projectTasks.forEach { task ->
                    if (task.endedAt > 0) {
                        spentTime += task.endedAt - task.startedAt
                    } else {
                        isRunning = true
                        startTime = task.startedAt
                        spentTime += System.currentTimeMillis() - task.startedAt
                    }
                }
                return@map UiProject(
                    id = project.id,
                    title = project.title,
                    order = project.order,
                    isRunning = isRunning,
                    spentTime = spentTime,
                    startTime = startTime,
                    color = project.color,
                    categoryId = project.categoryId
                )
            }
        }
    }

    suspend fun addProject(title: String, color: Int, category: String) {
        val categories = categoryDao.getAll().first()
        val lastOrder = categoryDao.getLastOrder() ?: 1
        val categoryId = if (category.trim().isNotEmpty()) {
            val cat = categories.filter { it.title == category.trim() }
            if (cat.isNotEmpty()) {
                cat.first().id
            } else {
                categoryDao.put(
                    Category(
                        title = category,
                        order = lastOrder + 1
                    )
                )
            }
        } else {
            Category.INCOMING_ID
        }
        projectDao.put(
            Project(
                title = title,
                order = -1,
                color = color,
                categoryId = categoryId
            )
        )
    }
}