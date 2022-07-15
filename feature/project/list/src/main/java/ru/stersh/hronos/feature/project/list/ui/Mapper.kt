package ru.stersh.hronos.feature.project.list.ui

import ru.stersh.hronos.feature.project.list.domain.Category
import ru.stersh.hronos.feature.project.list.domain.Project
import ru.stersh.hronos.feature.project.list.domain.Section
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.CategoryUi
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.ProjectUi
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.SectionUi

internal fun Section.toUi(): SectionUi {
    return SectionUi(
        category = category.toUi(),
        projects = projects
            .map { it.toUi() }
            .toMutableList()
    )
}

private fun Category.toUi(): CategoryUi {
    return CategoryUi(id, title)
}

private fun Project.toUi(): ProjectUi {
    return ProjectUi(id, title, order, isRunning, spentTime, startTime, color, categoryId)
}