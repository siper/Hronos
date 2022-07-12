package ru.stersh.hronos.ui.project.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState

interface ProjectsView : MvpView {
    @SingleState
    fun updateSections(sections: List<UiProjectSection>)

    @SingleState
    fun showEmptyView()

    @AddToEndSingle
    fun showAddProjectButton()

    @AddToEndSingle
    fun showStopTaskButton()
}