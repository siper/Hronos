package ru.stersh.hronos.feature.project.editor

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.stersh.hronos.core.RxPresenter
import ru.stersh.hronos.feature.project.core.ProjectsInteractor
import timber.log.Timber

class EditorProjectPresenter(private val interactor: ProjectsInteractor) :
    RxPresenter<EditorProjectView>() {
    fun addProject(title: String, color: Int) {
        if (title.isEmpty() || title.isBlank()) {
            viewState.showError()
            return
        }
        interactor
            .addProject(title, color)
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