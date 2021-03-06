package ru.stersh.hronos.feature.project.list.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.stersh.hronos.core.room.project.ProjectDao
import ru.stersh.hronos.core.room.project.ProjectDb
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.ProjectUi
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.SectionUi

internal class ProjectsAdapterDataProvider(private val projectDao: ProjectDao) {
    private val data = mutableListOf<SectionUi>()

    fun getGroupId(groupPosition: Int) = data[groupPosition].category.id

    fun getChildId(groupPosition: Int, childPosition: Int) =
        data[groupPosition].projects[childPosition].id

    fun getGroupCount() = data.size

    fun getChildCount(groupPosition: Int) = data[groupPosition].projects.size

    fun getGroup(groupPosition: Int) = data[groupPosition].category

    fun getChild(groupPosition: Int, childPosition: Int) =
        data[groupPosition].projects[childPosition]

    fun getLastGroupPosition() = data.size - 1

    fun setData(newData: List<SectionUi>) {
        data.clear()
        data.addAll(newData)
    }

    fun moveChild(
        fromGroupPosition: Int,
        fromChildPosition: Int,
        toGroupPosition: Int,
        toChildPosition: Int
    ) {
        if (fromGroupPosition == toGroupPosition && fromChildPosition == toChildPosition) return

        val itemsToUpdate = mutableListOf<ProjectUi>()

        // Получение элемента
        val item = data[fromGroupPosition].projects[fromChildPosition]
        // Удаляем элемент из первой группы
        data[fromGroupPosition].projects.remove(item)

        // Добавляем новый элемент в группу
        data[toGroupPosition].projects.add(toChildPosition, item)

        // Заносим элементы требующие обновление в базе в список из первой группы
        val fromCategoryId = data[fromGroupPosition].category.id
        data[fromGroupPosition].projects.forEachIndexed { index, uiProject ->
            if (index != uiProject.order || fromCategoryId != uiProject.categoryId) {
                itemsToUpdate.add(uiProject.copy(order = index, categoryId = fromCategoryId))
            }
        }

        if (toGroupPosition != fromGroupPosition) {
            // Заносим элементы требующие обновления из второй группы
            val toCategoryId = data[toGroupPosition].category.id
            data[toGroupPosition].projects.forEachIndexed { index, uiProject ->
                if (index != uiProject.order || toCategoryId != uiProject.categoryId) {
                    itemsToUpdate.add(uiProject.copy(order = index, categoryId = toCategoryId))
                }
            }
        }

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val projects = itemsToUpdate
                    .map { ProjectDb(it.id, it.title, it.order, it.color, it.categoryId) }
                projectDao.putAll(projects)
            }
        }
    }
}