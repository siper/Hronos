package ru.stersh.hronos.feature.project.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder
import ru.stersh.hronos.R
import ru.stersh.hronos.core.ui.TimerView

class ProjectViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
    val root = itemView.findViewById<MaterialCardView>(R.id.item_project)
    val title = itemView.findViewById<TextView>(R.id.title)
    val timeSpent = itemView.findViewById<TimerView>(R.id.time_spent)
    val startStop = itemView.findViewById<ImageView>(R.id.start_stop)
}