package ru.stersh.hronos.feature.project.main

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.stersh.hronos.core.RxPresenter
import ru.stersh.hronos.feature.category.UiCategory
import ru.stersh.hronos.feature.project.core.ProjectsInteractor
import timber.log.Timber

class ProjectsPresenter(private val interactor: ProjectsInteractor) : RxPresenter<ProjectsView>() {
    private val currentData = mutableListOf<UiProject>()

    override fun onFirstViewAttach() {
        val categories = interactor.getCategories()
        val projects = interactor.getProjects()
        Flowable.combineLatest(
            categories,
            projects,
            BiFunction<List<UiCategory>, List<UiProject>, Pair<List<UiCategory>, List<UiProject>>> { c, p ->
                return@BiFunction Pair(c, p)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.second.isEmpty()) {
                        viewState.showEmptyView()
                        viewState.showAddProjectButton()
                        return@subscribe
                    }
                    currentData.clear()
                    currentData.addAll(it.second)
                    viewState.updateProjects(it.second, it.first)
                    val hasRunningTask = it.second.filter { it.isRunning }.isNotEmpty()
                    if (hasRunningTask) {
                        viewState.showStopTaskButton()
                    } else {
                        viewState.showAddProjectButton()
                    }
                },
                {
                    Timber.e(it)
                }
            )
            .addTo(presenterLifecycle)
    }

    fun onStartStopClick(project: UiProject) {
        if (project.isRunning) {
            interactor
                .stopTask(project.id)
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(presenterLifecycle)
        } else {
            Observable
                .fromIterable(currentData)
                .flatMapCompletable { interactor.stopTask(it.id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        interactor
                            .startTask(project.id)
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                            .addTo(presenterLifecycle)
                    },
                    {
                        Timber.e(it)
                    }
                )
                .addTo(presenterLifecycle)
        }
    }

    fun stopRunningTask() {
        Observable
            .fromIterable(currentData.filter { it.isRunning })
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { interactor.stopTask(it.id) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                },
                {
                    Timber.e(it)
                }
            )
            .addTo(presenterLifecycle)
    }
}