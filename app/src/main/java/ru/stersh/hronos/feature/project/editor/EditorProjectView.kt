package ru.stersh.hronos.feature.project.editor

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface EditorProjectView : MvpView {
    @OneExecution
    fun done()

    @SingleState
    fun showError()
}