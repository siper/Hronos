package ru.stersh.hronos.ui.project.list

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stersh.hronos.R
import ru.stersh.hronos.databinding.FragmentProjectsBinding
import ru.stersh.hronos.ui.project.editor.ProjectEditorDialog

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding: FragmentProjectsBinding
        get() = _binding!!

    private val viewModel: ProjectsViewModel by viewModel()

    private val dataProvider by inject<ProjectsAdapterDataProvider>()

    private val adapter by lazy {
        ProjectsAdapter(dataProvider) { viewModel.onStartStopClick(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.sections.collect { sections ->
                binding.content.visibility = View.VISIBLE
                dataProvider.setData(sections)
                adapter.notifyDataSetChanged()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.mainButtonState.collect { mainButtonState ->
                if (mainButtonState == ProjectsViewModel.MainButtonStateUi.STOP_TASK) {
                    showStopTaskButton()
                } else {
                    showAddProjectButton()
                }
            }
        }
    }

    private fun showAddProjectButton() {
        binding.projectAddBtn.setText(R.string.add_project_title)
        binding.projectAddBtn.setIconResource(R.drawable.ic_add_black_24dp)
        binding.projectAddBtn.setIconTintResource(R.color.colorWhite)
        binding.projectAddBtn.setOnClickListener {
            val dialog = ProjectEditorDialog()
            dialog.show(parentFragmentManager, null)
        }
    }

    private fun showStopTaskButton() {
        binding.projectAddBtn.setText(R.string.stop_task_title)
        binding.projectAddBtn.setIconResource(R.drawable.ic_stop)
        binding.projectAddBtn.setIconTintResource(R.color.colorWhite)
        binding.projectAddBtn.setOnClickListener {
            viewModel.stopRunningTasks()
        }
    }

    private fun initAdapter(savedInstanceState: Bundle?) {
        val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        val savedState = savedInstanceState?.getParcelable<Parcelable>(PREVIOUS_SAVE_STATE)
        val expMgr = RecyclerViewExpandableItemManager(savedState)

        val dragAndDropManager = RecyclerViewDragDropManager()
        var wrappedAdapter = expMgr.createWrappedAdapter(adapter)
        wrappedAdapter = dragAndDropManager.createWrappedAdapter(wrappedAdapter)

        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    wrappedAdapter.getItemViewType(position) < 0 -> 2
                    else -> 1
                }
            }
        }

        dragAndDropManager.isCheckCanDropEnabled = true
        dragAndDropManager.setInitiateOnLongPress(true)
        dragAndDropManager.setInitiateOnTouch(false)
        dragAndDropManager.setInitiateOnMove(false)

        with(binding.content) {
            layoutManager = lm
            setHasFixedSize(true)
            adapter = wrappedAdapter
            expMgr.attachRecyclerView(this)
            dragAndDropManager.attachRecyclerView(this)
            addItemDecoration(ProjectsDivider())
        }
    }

    companion object {
        const val PREVIOUS_SAVE_STATE = "list_state"
    }
}