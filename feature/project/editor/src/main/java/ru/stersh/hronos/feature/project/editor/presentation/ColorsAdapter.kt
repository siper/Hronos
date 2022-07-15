package ru.stersh.hronos.feature.project.editor.presentation

import android.content.res.ColorStateList
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ru.stersh.hronos.core.utils.extention.hide
import ru.stersh.hronos.core.utils.extention.show
import ru.stersh.hronos.feature.project.editor.R

internal class ColorsAdapter(
    onColorClick: (UiColor) -> Unit
) : ListDelegationAdapter<List<UiColor>>(colorItem(onColorClick)) {

    companion object {
        fun colorItem(
            onColorClick: (UiColor) -> Unit
        ) = adapterDelegate<UiColor, UiColor>(R.layout.item_color) {
            val color = findViewById<ImageView>(R.id.color_view)
            val colorSelected = findViewById<ImageView>(R.id.color_selected)
            val root = findViewById<FrameLayout>(R.id.item_color)

            root.setOnClickListener {
                onColorClick.invoke(item)
            }

            bind {
                if (item.isSelected) {
                    colorSelected.show()
                } else {
                    colorSelected.hide()
                }
                ImageViewCompat.setImageTintList(color, ColorStateList.valueOf(item.color))
            }
        }
    }
}