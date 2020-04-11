package ru.stersh.hronos.feature.project.core

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import ru.stersh.hronos.core.entity.db.Project
import ru.stersh.hronos.core.entity.db.Task
import ru.stersh.hronos.feature.project.main.UiProject
import ru.stersh.hronos.task.model.repository.TaskDao

class ProjectsInteractor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao
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
                        spentTime = spentTime
                    )
                }
            }
        )
    }

    fun stopTask(projectId: Int): Completable {
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

    fun startTask(projectId: Int): Completable {
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

    fun addProject(title: String): Completable {
        return Completable.fromCallable { projectDao.put(Project(title = title, order = -1)) }
    }
}