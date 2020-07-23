package ru.stersh.hronos.ui.project.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.expandable.GroupPositionItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import ru.stersh.hronos.R

class ProjectsAdapter(private val startStopCallback: (UiProject) -> Unit) :
    AbstractExpandableItemAdapter<SectionViewHolder, ProjectViewHolder>(),
    ExpandableDraggableItemAdapter<SectionViewHolder, ProjectViewHolder> {
    private var data = mutableListOf<UiProjectSection>()

    init {
        setHasStableIds(true)
    }

    fun setData(newData: List<UiProjectSection>) {
        data.clear()
        data.addAll(newData)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_project_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindChildViewHolder(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int,
        viewType: Int
    ) {
        val project = data[groupPosition].projects[childPosition]
        holder.bind(project, startStopCallback)
    }

    override fun onBindGroupViewHolder(
        holder: SectionViewHolder,
        groupPosition: Int,
        viewType: Int
    ) {
        val category = data[groupPosition].category
        holder.bind(category)
    }

    override fun getGroupCount(): Int = data.size

    override fun getChildCount(groupPosition: Int): Int = data[groupPosition].projects.size

    override fun getGroupId(groupPosition: Int): Long = data[groupPosition].category.id

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return data[groupPosition].projects[childPosition].id
    }

    override fun getInitialGroupExpandedState(groupPosition: Int) = true

    override fun onGroupDragFinished(
        fromGroupPosition: Int,
        toGroupPosition: Int,
        result: Boolean
    ) {
    }

    override fun onMoveGroupItem(fromGroupPosition: Int, toGroupPosition: Int) {

    }

    override fun onCheckGroupCanDrop(draggingGroupPosition: Int, dropGroupPosition: Int): Boolean {
        return false
    }

    override fun onMoveChildItem(
        fromGroupPosition: Int,
        fromChildPosition: Int,
        toGroupPosition: Int,
        toChildPosition: Int
    ) {

    }

    override fun onCheckGroupCanStartDrag(
        holder: SectionViewHolder,
        groupPosition: Int,
        x: Int,
        y: Int
    ): Boolean {
        return false
    }

    override fun onCheckChildCanDrop(
        draggingGroupPosition: Int,
        draggingChildPosition: Int,
        dropGroupPosition: Int,
        dropChildPosition: Int
    ): Boolean {
        return true
    }

    override fun onGetGroupItemDraggableRange(
        holder: SectionViewHolder,
        groupPosition: Int
    ): ItemDraggableRange? {
        return null
    }

    override fun onChildDragStarted(groupPosition: Int, childPosition: Int) {

    }

    override fun onGetChildItemDraggableRange(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int
    ): ItemDraggableRange {
        return GroupPositionItemDraggableRange(groupPosition, groupPosition)
    }

    override fun onChildDragFinished(
        fromGroupPosition: Int,
        fromChildPosition: Int,
        toGroupPosition: Int,
        toChildPosition: Int,
        result: Boolean
    ) {

    }

    override fun onGroupDragStarted(groupPosition: Int) {}

    override fun onCheckChildCanStartDrag(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int,
        x: Int,
        y: Int
    ): Boolean {
        return true
    }

    override fun onCheckCanExpandOrCollapseGroup(
        holder: SectionViewHolder,
        groupPosition: Int,
        x: Int,
        y: Int,
        expand: Boolean
    ): Boolean {
        return true
    }
}