package ru.stersh.hronos.feature.project.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState

interface ProjectsView : MvpView {
    @SingleState
    fun updateProjects(projects: List<UiProject>)

    @SingleState
    fun showEmptyView()

    @AddToEndSingle
    fun showAddProjectButton()

    @AddToEndSingle
    fun showStopTaskButton()
}