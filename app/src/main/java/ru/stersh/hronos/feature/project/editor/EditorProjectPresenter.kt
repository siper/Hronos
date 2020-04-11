package ru.stersh.hronos.feature.project.editor

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.stersh.hronos.core.RxPresenter
import ru.stersh.hronos.feature.project.core.ProjectsInteractor
import timber.log.Timber

class EditorProjectPresenter(
    private val interactor: ProjectsInteractor
) : RxPresenter<EditorProjectView>() {
    private var suggestionsDisposable: Disposable? = null

    fun addProject(title: String, color: Int, category: String) {
        if (title.isEmpty() || title.isBlank()) {
            viewState.showError()
            return
        }
        interactor
            .addProject(title, color, category)
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

    fun requestSuggestions(query: String) {
        if (query.isEmpty() || query.isBlank()) return
        suggestionsDisposable?.dispose()
        suggestionsDisposable = interactor
            .getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    viewState.fillSuggestions(
                        it
                            .map { it.title }
                            .filter { it.toLowerCase().contains(query.toLowerCase()) }
                    )
                },
                {
                    Timber.e(it)
                }
            )
    }
}