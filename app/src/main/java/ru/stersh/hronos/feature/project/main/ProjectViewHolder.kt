package ru.stersh.hronos.feature.project.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ru.stersh.hronos.R
import ru.stersh.hronos.core.ui.TimerView

class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val root = itemView.findViewById<MaterialCardView>(R.id.item_project)
    val title = itemView.findViewById<TextView>(R.id.title)
    val timeSpent = itemView.findViewById<TimerView>(R.id.time_spent)
    val startStop = itemView.findViewById<ImageView>(R.id.start_stop)
}