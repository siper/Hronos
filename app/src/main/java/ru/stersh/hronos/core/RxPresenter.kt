package ru.stersh.hronos.core

import io.reactivex.disposables.CompositeDisposable
import moxy.MvpPresenter
import moxy.MvpView

abstract class RxPresenter<T : MvpView> : MvpPresenter<T>() {
    internal val presenterLifecycle = CompositeDisposable()
    internal val viewLifecycle = CompositeDisposable()

    override fun detachView(view: T) {
        super.detachView(view)
        viewLifecycle.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterLifecycle.dispose()
        viewLifecycle.dispose()
    }
}