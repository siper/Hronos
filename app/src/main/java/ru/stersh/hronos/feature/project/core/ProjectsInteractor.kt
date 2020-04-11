package ru.stersh.hronos.feature.project.core

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
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

    fun changeProjectOrder(project: Project, newOrder: Int): Completable {
        return projectDao
            .getAllProjects()
            .map {
                val result = it.toMutableList().apply {
                    remove(project)
                    add(newOrder, project)
                    mapIndexed { index, project ->
                        project.copy(order = index)
                    }
                    toList()
                }
                projectDao.putAll(*it.toTypedArray())
                result
            }
            .ignoreElements()
    }

    fun getCategories(): Flowable<List<UiCategory>> {
        return categoryDao
            .getAll()
            .map {
                val categories = it.toMutableList()
                categories.add(0, Category(-1, "Unsorted"))
                return@map categories.map { UiCategory(it.id, it.title) }.toList()
            }
    }

    fun getProjects(): Flowable<List<UiProject>> {
        val tasks = taskDao.getAll()
        val projects = projectDao.getAllProjects()
        return Flowable.combineLatest(
            projects,
            tasks,
            BiFunction { p, t ->
                p.map { project ->
                    val projectTasks = t.filter { it.projectId == project.id }
                    var spentTime = 0L
                    var isRunning = false
                    projectTasks.forEach { task ->
                        if (task.endedAt > 0) {
                            spentTime += task.endedAt - task.startedAt
                        } else {
                            isRunning = true
                            spentTime += System.currentTimeMillis() - task.startedAt
                        }
                    }
                    return@map UiProject(
                        id = project.id,
                        title = project.title,
                        order = project.order,
                        isRunning = isRunning,
                        spentTime = spentTime,
                        color = project.color,
                        categoryId = project.categoryId
                    )
                }
            }
        )
    }

    fun stopTask(projectId: Long): Completable {
        return taskDao
            .getByProjectId(projectId)
            .map { it.filter { it.endedAt == 0L } }
            .firstElement()
            .doOnSuccess { tasks ->
                tasks.forEach { task ->
                    taskDao.put(task.copy(endedAt = System.currentTimeMillis()))
                }
            }
            .ignoreElement()
    }

    fun startTask(projectId: Long): Completable {
        return Completable.fromCallable {
            taskDao.put(
                Task(
                    projectId = projectId,
                    title = "",
                    endedAt = 0L,
                    startedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun addProject(title: String, color: Int): Completable {
        return Completable.fromCallable {
            projectDao.put(
                Project(
                    title = title,
                    order = -1,
                    color = color
                )
            )
        }
    }
}