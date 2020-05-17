package ru.stersh.hronos.feature.project.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.stersh.hronos.feature.category.UiCategory
import ru.stersh.hronos.feature.category.core.Category
import ru.stersh.hronos.feature.category.core.CategoryDao
import ru.stersh.hronos.feature.project.main.UiProject
import ru.stersh.hronos.feature.task.core.Task
import ru.stersh.hronos.feature.task.core.TaskDao

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
            .map {
                val categories = it.toMutableList()
                categories.add(0, Category(-1, "Unsorted"))
                return@map categories.map { UiCategory(it.id, it.title) }.toList()
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

    suspend fun stopTask(projectId: Long) {
        val tasks = taskDao
            .getByProjectId(projectId)
            .filter { it.endedAt == 0L }
            .map { it.copy(endedAt = System.currentTimeMillis()) }
        tasks.forEach { task ->
            taskDao.put(task.copy(endedAt = System.currentTimeMillis()))
        }
    }

    suspend fun stopRunningTasks() {
        val tasks = taskDao
            .getAll()
            .first()
            .filter { it.endedAt == 0L }
            .map { it.copy(endedAt = System.currentTimeMillis()) }
        taskDao.put(*tasks.toTypedArray())
    }

    suspend fun startTask(projectId: Long) {
        taskDao.put(
            Task(
                projectId = projectId,
                title = "",
                endedAt = 0L,
                startedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addProject(title: String, color: Int, category: String) {
        val categories = getCategories().first()
        val categoryId = if (category.trim().isNotEmpty()) {
            val cat = categories.filter { it.title == category.trim() }
            if (cat.isNotEmpty()) {
                cat.first().id
            } else {
                categoryDao.put(Category(title = category))
            }
        } else {
            -1
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