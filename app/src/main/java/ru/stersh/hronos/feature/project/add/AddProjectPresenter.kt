package ru.stersh.hronos.feature.project.add

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.stersh.hronos.core.RxPresenter
import ru.stersh.hronos.feature.project.core.ProjectsInteractor
import timber.log.Timber

class AddProjectPresenter(private val interactor: ProjectsInteractor) :
    RxPresenter<AddProjectView>() {
    fun addProject(title: String) {
        if (title.isEmpty() || title.isBlank()) {
            viewState.showError()
            return
        }
        interactor
            .addProject(title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    viewState.done()
                },
                {
                    Timber.e(it)
                }
            )
            .addTo(presenterLifecycle)
    }
}