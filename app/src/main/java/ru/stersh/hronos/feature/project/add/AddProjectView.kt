package ru.stersh.hronos.feature.project.add

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface AddProjectView : MvpView {
    @OneExecution
    fun done()

    @SingleState
    fun showError()
}