package ru.stersh.hronos.feature.project.main

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.stersh.hronos.R

class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val root = itemView.findViewById<LinearLayout>(R.id.item_project_section)
    val title = itemView.findViewById<TextView>(R.id.section_title)
}