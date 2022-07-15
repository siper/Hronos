package ru.stersh.hronos.navigation

import androidx.fragment.app.FragmentManager
import ru.stersh.hronos.feature.project.editor.presentation.ProjectEditorDialog
import ru.stersh.hronos.feature.project.list.navigation.ProjectListNavigation

class ProjectListNavigationImpl : ProjectListNavigation {
    override fun openProjectEditor(fragmentManager: FragmentManager) {
        val dialog = ProjectEditorDialog()
        dialog.show(fragmentManager, null)
    }
}