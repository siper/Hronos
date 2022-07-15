package ru.stersh.hronos.feature.project.list.ui.adapter.holder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import ru.stersh.hronos.feature.project.list.R
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.CategoryUi

internal class SectionViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView) {
    val root = itemView.findViewById<LinearLayout>(R.id.item_project_section)
    val title = itemView.findViewById<TextView>(R.id.section_title)

    fun bind(category: CategoryUi) {
        title.text = category.title
    }
}