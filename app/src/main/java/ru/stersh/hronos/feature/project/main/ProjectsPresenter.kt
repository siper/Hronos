package ru.stersh.hronos.feature.project.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.stersh.hronos.core.RxPresenter
import ru.stersh.hronos.feature.project.core.ProjectsInteractor
import timber.log.Timber

class ProjectsPresenter(private val interactor: ProjectsInteractor) : RxPresenter<ProjectsView>() {
    private val currentData = mutableListOf<UiProject>()

    override fun onFirstViewAttach() {
        interactor
            .getProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        viewState.showEmptyView()
                        return@subscribe
                    }
                    currentData.clear()
                    currentData.addAll(it)
                    viewState.updateProjects(it)
                    val hasRunningTask = currentData.filter { it.isRunning }.isNotEmpty()
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
                .fromIterable(currentData.filter { it.isRunning })
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { interactor.stopTask(it.id) }
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