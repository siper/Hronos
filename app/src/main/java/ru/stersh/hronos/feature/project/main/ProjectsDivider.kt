package ru.stersh.hronos.feature.project.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.stersh.hronos.extention.dp

class ProjectsDivider(private val margin: Int = 16.dp) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = margin
        outRect.right = margin
        outRect.top = margin / 2
        outRect.bottom = margin / 2
        val adapter = parent.adapter
        if (adapter != null) {
            val currentPosition = parent.getChildAdapterPosition(view)
            val isLastPosition = adapter.itemCount == currentPosition + 1
            if (isLastPosition) {
                outRect.bottom = 80.dp
            }
        }
    }
}