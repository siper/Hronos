package ru.stersh.hronos.feature.project.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState
import ru.stersh.hronos.feature.category.UiCategory

interface ProjectsView : MvpView {
    @SingleState
    fun updateSections(sections: List<ProjectSection>)

    @SingleState
    fun showEmptyView()

    @AddToEndSingle
    fun showAddProjectButton()

    @AddToEndSingle
    fun showStopTaskButton()
}