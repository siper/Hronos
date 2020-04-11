package ru.stersh.hronos.feature.project.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState
import ru.stersh.hronos.feature.category.UiCategory

interface ProjectsView : MvpView {
    @SingleState
    fun updateProjects(projects: List<UiProject>, categories: List<UiCategory>)

    @SingleState
    fun showEmptyView()

    @AddToEndSingle
    fun showAddProjectButton()

    @AddToEndSingle
    fun showStopTaskButton()
}