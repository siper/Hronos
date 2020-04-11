package ru.stersh.hronos.feature.project.main

import ru.stersh.hronos.core.entity.ui.UiItem

data class UiProject(
    val id: Int,
    val title: String,
    val order: Int,
    val isRunning: Boolean,
    val spentTime: Long
) : UiItem