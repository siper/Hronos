package ru.stersh.hronos.ui.project.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.stersh.hronos.extention.dp

class ProjectsDivider(private val margin: Int = 16.dp) : RecyclerView.ItemDecoration() {
    private var firstItemInSectionPosition = -1

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(view)
        val currentPosition = viewHolder.bindingAdapterPosition
        if (viewHolder is ProjectViewHolder) {
            val column =
                if (currentPosition - firstItemInSectionPosition == 0 || (currentPosition - firstItemInSectionPosition) % 2 == 0) {
                    1
                } else {
                    2
                }
            when (column) {
                1 -> {
                    outRect.left = margin
                    outRect.right = margin / 2
                }
                2 -> {
                    outRect.left = margin / 2
                    outRect.right = margin
                }
            }
            if (currentPosition != firstItemInSectionPosition && currentPosition != firstItemInSectionPosition + 1) {
                outRect.top = margin
            }
        }
        if (viewHolder is SectionViewHolder) {
            firstItemInSectionPosition = currentPosition + 1
        }
    }
}
