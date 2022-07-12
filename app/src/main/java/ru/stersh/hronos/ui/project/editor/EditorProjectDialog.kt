package ru.stersh.hronos.ui.project.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_add_project.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di

class EditorProjectDialog : MvpBottomSheetDialogFragment(), EditorProjectView {
    private val presenter by moxyPresenter { EditorProjectPresenter(Di.get(), Di.get()) }

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
            presenter.addProject(
                project_title.text.toString(),
                color,
                project_category.text.toString()
            )
        }
        colors.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        colors.adapter = adapter
        adapter.items = resources.getIntArray(R.array.project_colors).mapIndexed { index, i ->
            UiColor(index, i, index == 0)
        }
        project_category.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    presenter.requestSuggestions(it.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun done() {
        dismiss()
    }

    override fun showError() {
        project_input_layout.error = getString(R.string.project_name_error)
    }

    override fun fillSuggestions(suggestions: List<String>) {
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            suggestions
        )
        project_category.setAdapter(adapter)
    }
}