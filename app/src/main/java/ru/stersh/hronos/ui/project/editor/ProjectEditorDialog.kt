package ru.stersh.hronos.ui.project.editor

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stersh.hronos.R
import ru.stersh.hronos.databinding.DialogAddProjectBinding

class ProjectEditorDialog : BottomSheetDialogFragment() {

    private var _binding: DialogAddProjectBinding? = null
    private val binding: DialogAddProjectBinding
        get() = _binding!!

    private val viewModel: ProjectEditorViewModel by viewModel()

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
    ): View {
        _binding = DialogAddProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.done.setOnClickListener {
            val color = adapter.items
                .filter { it.isSelected }
                .map { it.id }
                .firstOrNull() ?: return@setOnClickListener
            viewModel.addProject(
                binding.projectTitle.text.toString(),
                color,
                binding.projectCategory.text.toString()
            )
        }
        binding.colors.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.colors.adapter = adapter
        adapter.items = resources.getIntArray(R.array.project_colors).mapIndexed { index, i ->
            UiColor(index, i, index == 0)
        }
        binding.projectCategory.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    viewModel.requestSuggestions(it.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        lifecycleScope.launchWhenStarted {
            viewModel.exit.collect {
                dismiss()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect {
                binding.projectInputLayout.error = if (it) {
                     getString(R.string.project_name_error)
                } else {
                    null
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.suggestions.collect {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_dropdown_item_1line,
                    it
                )
                binding.projectCategory.setAdapter(adapter)
            }
        }
    }
}