package ru.stersh.hronos.feature.project.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.dialog_add_project.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di

class EditorProjectDialog : MvpBottomSheetDialogFragment(), EditorProjectView {
    private val presenter by moxyPresenter { EditorProjectPresenter(Di.get()) }

    private val adapter: ColorsAdapter by lazy {
        ColorsAdapter { clickedColor ->
            val items = adapter.items.map {
                if (it.id == clickedColor.id) {
                    it.copy(isSelected = true)
                } else {
                    it.copy(isSelected = false)
                }
            }
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.setOnClickListener {
            val color = adapter.items
                .filter { it.isSelected }
                .map { it.id }
                .firstOrNull() ?: return@setOnClickListener
            presenter.addProject(project_title.text.toString(), color)
        }
        colors.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        colors.adapter = adapter
        adapter.items = resources.getIntArray(R.array.project_colors).mapIndexed { index, i ->
            UiColor(index, i, index == 0)
        }
    }

    override fun done() {
        dismiss()
    }

    override fun showError() {
        project_input_layout.error = getString(R.string.project_name_error)
    }
}