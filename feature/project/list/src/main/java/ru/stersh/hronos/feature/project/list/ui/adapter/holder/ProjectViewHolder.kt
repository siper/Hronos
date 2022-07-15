package ru.stersh.hronos.feature.project.list.ui.adapter.holder

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import com.google.android.material.card.MaterialCardView
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder
import ru.stersh.hronos.core.ui.TimerView
import ru.stersh.hronos.feature.project.list.R
import ru.stersh.hronos.feature.project.list.ui.adapter.entity.ProjectUi

internal class ProjectViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
    val root = itemView.findViewById<MaterialCardView>(R.id.item_project)
    val title = itemView.findViewById<TextView>(R.id.title)
    val timeSpent = itemView.findViewById<TimerView>(R.id.time_spent)
    val startStop = itemView.findViewById<ImageView>(R.id.start_stop)

    val colors = itemView.resources.getIntArray(R.array.project_colors)

    fun bind(project: ProjectUi, startStopCallback: (ProjectUi) -> Unit) {
        title.text = project.title

        timeSpent.setTextColor(colors[project.color])
        timeSpent.apply {
            baseTime = project.spentTime
            startTime = project.startTime
            if (project.isRunning) start() else stop()
        }

        if (project.isRunning) {
            startStop.setImageResource(R.drawable.ic_stop)
        } else {
            startStop.setImageResource(R.drawable.ic_start)
        }
        startStop.setOnClickListener { startStopCallback.invoke(project) }
        ImageViewCompat.setImageTintList(
            startStop,
            ColorStateList.valueOf(colors[project.color])
        )
    }
}