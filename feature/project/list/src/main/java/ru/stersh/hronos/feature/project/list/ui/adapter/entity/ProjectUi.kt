package ru.stersh.hronos.feature.project.list.ui.adapter.entity

internal data class ProjectUi(
    val id: Long,
    val title: String,
    val order: Int,
    val isRunning: Boolean,
    val spentTime: Long,
    val startTime: Long,
    val color: Int,
    val categoryId: Long
)