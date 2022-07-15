package ru.stersh.hronos.feature.project.list.navigation

import androidx.fragment.app.FragmentManager

interface ProjectListNavigation {
    fun openProjectEditor(fragmentManager: FragmentManager)
}