package ru.stersh.hronos.ui.project.main

import ru.stersh.hronos.core.entity.ui.UiItem

data class UiProject(
    val id: Long,
    val title: String,
    val order: Int,
    val isRunning: Boolean,
    val spentTime: Long,
    val startTime: Long,
    val color: Int,
    val categoryId: Long
) : UiItem