package ru.stersh.hronos.feature.project.main

import ru.stersh.hronos.feature.category.UiCategory

class ProjectDataProvider() {
    private val data = mutableListOf<Section>()

    fun setData(projects: List<UiProject>, categories: List<UiCategory>) {
        data.apply {
            clear()
            categories.forEach { category ->
                add(Section(category, projects.filter { it.categoryId == category.id }))
            }
        }
    }

    fun getGroupCount() = data.size

    fun getItemCount(groupPosition: Int) = data[groupPosition].projects.size

    fun getGroupId(groupPosition: Int) = data[groupPosition].section.id

    fun getItemId(groupPosition: Int, itemPosition: Int) =
        data[groupPosition].projects[itemPosition].id

    fun getItem(groupPosition: Int, itemPosition: Int) = data[groupPosition].projects[itemPosition]

    fun getGroup(groupPosition: Int) = data[groupPosition].section

    data class Section(val section: UiCategory, val projects: List<UiProject>)
}