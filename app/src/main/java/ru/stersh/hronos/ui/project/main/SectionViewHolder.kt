package ru.stersh.hronos.ui.project.main

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import ru.stersh.hronos.R
import ru.stersh.hronos.ui.category.UiCategory

class SectionViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView) {
    val root = itemView.findViewById<LinearLayout>(R.id.item_project_section)
    val title = itemView.findViewById<TextView>(R.id.section_title)

    fun bind(category: UiCategory) {
        title.text = category.title
    }
}