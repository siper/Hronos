package ru.stersh.hronos.feature.project.main

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.expandable.ChildPositionItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import ru.stersh.hronos.R

class SProjectAdapter(
    private val dataProvider: ProjectDataProvider
) : AbstractExpandableItemAdapter<SectionViewHolder, ProjectViewHolder>(),
    ExpandableDraggableItemAdapter<SectionViewHolder, ProjectViewHolder> {


    init {
        setHasStableIds(true)
    }

    override fun getChildItemViewType(groupPosition: Int, childPosition: Int): Int {
        return CHILD_ITEM_ID
    }

    override fun getGroupItemViewType(groupPosition: Int): Int {
        return GROUP_ITEM_ID
    }

    override fun onGroupDragFinished(
        fromGroupPosition: Int,
        toGroupPosition: Int,
        result: Boolean
    ) {
        notifyDataSetChanged()
    }

    override fun onMoveGroupItem(fromGroupPosition: Int, toGroupPosition: Int) {
        notifyDataSetChanged()
    }

    override fun onCheckGroupCanDrop(draggingGroupPosition: Int, dropGroupPosition: Int): Boolean {
        return true
    }

    override fun onMoveChildItem(
        fromGroupPosition: Int,
        fromChildPosition: Int,
        toGroupPosition: Int,
        toChildPosition: Int
    ) {
        notifyDataSetChanged()
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
        notifyDataSetChanged()
    }

    override fun onGetChildItemDraggableRange(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int
    ): ItemDraggableRange? {
        val countChild = getChildCount(groupPosition)
        var start = groupPosition - 2
        val end = groupPosition + countChild
        if (start < 0) {
            start = 0
        }
        return ChildPositionItemDraggableRange(start, end)

    }

    override fun onChildDragFinished(
        fromGroupPosition: Int,
        fromChildPosition: Int,
        toGroupPosition: Int,
        toChildPosition: Int,
        result: Boolean
    ) {
        notifyDataSetChanged()
    }

    override fun onGroupDragStarted(groupPosition: Int) {
        notifyDataSetChanged()
    }

    override fun onCheckChildCanStartDrag(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int,
        x: Int,
        y: Int
    ): Boolean {
        return true
    }

    override fun getChildCount(groupPosition: Int): Int = dataProvider.getItemCount(groupPosition)

    override fun onCheckCanExpandOrCollapseGroup(
        holder: SectionViewHolder,
        groupPosition: Int,
        x: Int,
        y: Int,
        expand: Boolean
    ): Boolean {
        return false
    }

    override fun getInitialGroupExpandedState(groupPosition: Int): Boolean {
        return true
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_project_section, parent, false)
        return SectionViewHolder(v)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(v)
    }

    override fun getGroupId(groupPosition: Int): Long = dataProvider.getGroupId(groupPosition)

    override fun getChildId(groupPosition: Int, childPosition: Int): Long =
        dataProvider.getItemId(groupPosition, childPosition)

    override fun getGroupCount(): Int = dataProvider.getGroupCount()

    override fun onBindGroupViewHolder(
        holder: SectionViewHolder,
        groupPosition: Int,
        viewType: Int
    ) {
        val item = dataProvider.getGroup(groupPosition)
        holder.title.text = item.title
    }

    override fun onBindChildViewHolder(
        holder: ProjectViewHolder,
        groupPosition: Int,
        childPosition: Int,
        viewType: Int
    ) {
        val item = dataProvider.getItem(groupPosition, childPosition)
        val colors = holder.itemView.resources.getIntArray(R.array.project_colors)
        val alphaColors = holder.itemView.resources.getIntArray(R.array.project_alpha_colors)
        holder.root.setCardBackgroundColor(alphaColors[item.color])
        holder.title.text = item.title
        holder.title.setTextColor(colors[item.color])
        holder.timeSpent.setTextColor(colors[item.color])
        holder.timeSpent.apply {
            baseTime = item.spentTime
            if (item.isRunning) start() else stop()
        }
        if (item.isRunning) {
            holder.startStop.setImageResource(R.drawable.ic_stop)
        } else {
            holder.startStop.setImageResource(R.drawable.ic_start)
        }
        ImageViewCompat.setImageTintList(
            holder.startStop,
            ColorStateList.valueOf(colors[item.color])
        )
    }

    companion object {
        const val GROUP_ITEM_ID = 444
        const val CHILD_ITEM_ID = 555
    }
}