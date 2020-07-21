package ru.stersh.hronos.feature.project.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import ru.stersh.hronos.extention.dp

class ProjectsDivider(private val margin: Int = 16.dp) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter as SectionedRecyclerViewAdapter? ?: return
        val count = adapter.itemCount
        val sections = mutableListOf<Section>()
        val items = mutableListOf<Int>()
        var prevHeaderPosition = -1
        for (i in 0 until count) {
            when (adapter.getSectionItemViewType(i)) {
                SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER -> {
                    if (prevHeaderPosition != -1) {
                        sections.add(Section(prevHeaderPosition, items.toList()))
                        items.clear()
                    }
                    prevHeaderPosition = i
                }
                else -> items.add(i)

            }
            if (i == count - 1 && items.isNotEmpty() && prevHeaderPosition != -1) {
                sections.add(Section(prevHeaderPosition, items.toList()))
                items.clear()
            }
        }
        val viewPosition = parent.getChildAdapterPosition(view)
        if (adapter.getSectionItemViewType(viewPosition) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) return
        var positionInSection = -1
        var sectionItemsCount = -1
        var isLastSection = false
        sections.forEach { section ->
            section.items.forEachIndexed { index, i ->
                if (viewPosition == i) {
                    positionInSection = index + 1
                    sectionItemsCount = section.items.size
                    isLastSection = sections.indexOf(section) == sections.size - 1
                    return@forEachIndexed
                }
            }
        }
        if (positionInSection == -1) return
        if (positionInSection % 2 == 1) {
            outRect.left = margin
            outRect.right = margin / 2
        }
        if (positionInSection % 2 == 0) {
            outRect.right = margin
            outRect.left = margin / 2
        }
        outRect.bottom = margin
        if (sectionItemsCount == -1) return
        if (isLastSection) {
            if (sectionItemsCount % 2 == 0) {
                if (positionInSection in sectionItemsCount - 1..sectionItemsCount) {
                    outRect.bottom = margin * 2 + 56.dp
                }
            } else {
                if (positionInSection == sectionItemsCount) {
                    outRect.bottom = margin * 2 + 56.dp
                }
            }
        } else {
            if (sectionItemsCount % 2 == 0) {
                if (positionInSection in sectionItemsCount - 1..sectionItemsCount) {
                    outRect.bottom = 0.dp
                }
            } else {
                if (positionInSection == sectionItemsCount) {
                    outRect.bottom = 0.dp
                }
            }
        }
    }

    class Section(val headerPosition: Int, val items: List<Int>)
}
