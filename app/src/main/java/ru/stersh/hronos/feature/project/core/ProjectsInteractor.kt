package ru.stersh.hronos.feature.project.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.stersh.hronos.core.data.category.Category
import ru.stersh.hronos.core.data.category.CategoryDao
import ru.stersh.hronos.core.data.project.Project
import ru.stersh.hronos.core.data.project.ProjectDao
import ru.stersh.hronos.core.data.task.Task
import ru.stersh.hronos.core.data.task.TaskDao
import ru.stersh.hronos.feature.category.UiCategory
import ru.stersh.hronos.feature.project.main.UiProject

class ProjectsInteractor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) {

//    suspend fun changeProjectOrder(project: Project, newOrder: Int) {
//        val projects = projectDao
//            .getAllProjects()
//            .map { it.sortedByDescending { it.order } }
//            .sortedByDescending { it.order }
//            .toMutableList()
//        projects.remove(project)
//        projects.add(newOrder, project.copy(order = newOrder))
//        projects.mapIndexed { index, p -> p.copy(order = index) }
//        projectDao.putAll(*projects.toTypedArray())
//    }

    fun getCategories(): Flow<List<UiCategory>> {
        return categoryDao
            .getAll()
            .map { categories ->
                categories.map { UiCategory(it.id, it.title) }
            }
    }

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

    fun hasRunningTasks(): Flow<Boolean> {
        return taskDao.runningCount().map { it > 0 }
    }

    suspend fun stopTask(projectId: Long) {
        val tasks = taskDao
            .getRunning(projectId)
            .map { it.end() }
        taskDao.put(tasks)
    }

    suspend fun stopRunningTasks() {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
        taskDao.put(tasks)
    }

    suspend fun stopRuningAndStartTask(projectId: Long) {
        val tasks = taskDao
            .getRunning()
            .map { it.end() }
            .toMutableList()
            .apply { add(Task.start(projectId)) }
        taskDao.put(tasks)
    }

    suspend fun addProject(title: String, color: Int, category: String) {
        val categories = getCategories().first()
        val categoryId = if (category.trim().isNotEmpty()) {
            val cat = categories.filter { it.title == category.trim() }
            if (cat.isNotEmpty()) {
                cat.first().id
            } else {
                categoryDao.put(
                    Category(
                        title = category
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