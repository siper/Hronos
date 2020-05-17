package ru.stersh.hronos.feature.project.main

import android.content.res.ColorStateList
import android.view.View
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import ru.stersh.hronos.R
import ru.stersh.hronos.feature.category.UiCategory

class ProjectSection(
    val category: UiCategory,
    val data: List<UiProject>,
    val startStopCallback: (UiProject) -> Unit
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_project)
        .headerResourceId(R.layout.item_project_section)
        .build()
) {
    override fun getContentItemsTotal(): Int = data.size

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder !is ProjectViewHolder) return
        val item = data[position]
        val colors = holder.itemView.resources.getIntArray(R.array.project_colors)
        holder.title.text = item.title
        holder.timeSpent.setTextColor(colors[item.color])
        holder.timeSpent.apply {
            baseTime = item.spentTime
            startTime = item.startTime
            if (item.isRunning) start() else stop()
        }
        if (item.isRunning) {
            holder.startStop.setImageResource(R.drawable.ic_stop)
        } else {
            holder.startStop.setImageResource(R.drawable.ic_start)
        }
        holder.startStop.setOnClickListener { startStopCallback.invoke(item) }
        ImageViewCompat.setImageTintList(
            holder.startStop,
            ColorStateList.valueOf(colors[item.color])
        )
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ProjectViewHolder(view)
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return SectionViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        if (holder !is SectionViewHolder) return
        holder.title.text = category.title
    }
}