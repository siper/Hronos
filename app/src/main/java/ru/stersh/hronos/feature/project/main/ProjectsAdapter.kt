package ru.stersh.hronos.feature.project.main

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ru.stersh.hronos.R
import ru.stersh.hronos.core.entity.ui.UiItem
import ru.stersh.hronos.core.ui.TimerView

class ProjectsAdapter(
    onProjectClick: (UiProject) -> Unit,
    onStartStopClick: (UiProject) -> Unit
) : AsyncListDifferDelegationAdapter<UiItem>(DIFF_CALLBACK) {
    init {
        delegatesManager.addDelegate(
            projectItem(
                onProjectClick,
                onStartStopClick
            )
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiItem>() {
            override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                if (oldItem is UiProject && newItem is UiProject) {
                    return oldItem.id == newItem.id
                }
                return false
            }

            override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                if (oldItem is UiProject && newItem is UiProject) {
                    return oldItem.title == newItem.title
                            && oldItem.order == newItem.order
                            && oldItem.isRunning == newItem.isRunning
                            && oldItem.spentTime == newItem.spentTime
                            && oldItem.color == newItem.color
                }
                return false
            }
        }

        fun projectItem(
            onProjectClick: (UiProject) -> Unit,
            onStartStopClick: (UiProject) -> Unit
        ) = adapterDelegate<UiProject, UiItem>(R.layout.item_project) {
            val root = findViewById<MaterialCardView>(R.id.item_project)
            val title = findViewById<TextView>(R.id.title)
            val startStop = findViewById<ImageView>(R.id.start_stop)
            val timeSpent = findViewById<TimerView>(R.id.time_spent)

            root.setOnClickListener { onProjectClick.invoke(item) }
            startStop.setOnClickListener { onStartStopClick.invoke(item) }

            bind {
                val colors = itemView.resources.getIntArray(R.array.project_colors)
                val alphaColors = itemView.resources.getIntArray(R.array.project_alpha_colors)
                root.setCardBackgroundColor(alphaColors[item.color])
                title.text = item.title
                title.setTextColor(colors[item.color])
                timeSpent.setTextColor(colors[item.color])
                timeSpent.apply {
                    baseTime = item.spentTime
                    if (item.isRunning) start() else stop()
                }
                if (item.isRunning) {
                    startStop.setImageResource(R.drawable.ic_stop)
                } else {
                    startStop.setImageResource(R.drawable.ic_start)
                }
                ImageViewCompat.setImageTintList(
                    startStop,
                    ColorStateList.valueOf(colors[item.color])
                )
            }
        }
    }
}