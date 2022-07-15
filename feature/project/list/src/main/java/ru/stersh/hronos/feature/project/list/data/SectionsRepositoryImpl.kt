package ru.stersh.hronos.feature.project.list.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.stersh.hronos.core.room.category.CategoryDao
import ru.stersh.hronos.core.room.project.ProjectDao
import ru.stersh.hronos.core.room.task.TaskDao
import ru.stersh.hronos.core.utils.extention.mapItems
import ru.stersh.hronos.feature.project.list.domain.Category
import ru.stersh.hronos.feature.project.list.domain.Project
import ru.stersh.hronos.feature.project.list.domain.Section
import ru.stersh.hronos.feature.project.list.domain.SectionsRepository

internal class SectionsRepositoryImpl(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) : SectionsRepository {

    override fun getSections(): Flow<List<Section>> {
        return getProjects().combine(getCategories()) { projects, categories ->
            return@combine categories
                .map { category ->
                    Section(
                        category,
                        projects
                            .filter { it.categoryId == category.id }
                            .sortedBy { it.order }
                            .toMutableList()
                    )
                }
                .sortedBy { it.projects.count() }
        }
    }

    private fun getCategories(): Flow<List<Category>> {
        return categoryDao
            .getAll()
            .mapItems { Category(it.id, it.title) }
    }

    private fun getProjects(): Flow<List<Project>> {
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
                return@map Project(
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
}