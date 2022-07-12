package ru.stersh.hronos.ui.project.editor

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface EditorProjectView : MvpView {
    @OneExecution
    fun done()

    @SingleState
    fun showError()

    @AddToEndSingle
    fun fillSuggestions(suggestions: List<String>)
}