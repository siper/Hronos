package ru.stersh.hronos.feature.project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.fragment_projects.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di
import ru.stersh.hronos.feature.category.UiCategory
import ru.stersh.hronos.feature.project.editor.EditorProjectDialog

class ProjectsFragment : MvpAppCompatFragment(R.layout.fragment_projects),
    ProjectsView {
    val presenter by moxyPresenter { ProjectsPresenter(Di.get()) }

    val dataProvider = ProjectDataProvider()

    val adapter by lazy { SProjectAdapter(dataProvider) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    override fun updateProjects(projects: List<UiProject>, categories: List<UiCategory>) {
        content.visibility = View.VISIBLE
        dataProvider.setData(projects, categories)
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyView() {}

    override fun showAddProjectButton() {
        project_add_btn.setText(R.string.add_project_title)
        project_add_btn.setIconResource(R.drawable.ic_add_black_24dp)
        project_add_btn.setIconTintResource(R.color.colorWhite)
        project_add_btn.setOnClickListener {
            val dialog = EditorProjectDialog()
            dialog.show(parentFragmentManager, null)
        }
    }

    override fun showStopTaskButton() {
        project_add_btn.setText(R.string.stop_task_title)
        project_add_btn.setIconResource(R.drawable.ic_stop)
        project_add_btn.setIconTintResource(R.color.colorWhite)
        project_add_btn.setOnClickListener {
            presenter.stopRunningTask()
        }
    }

    private fun initAdapter() {
        val expandableItemManager = RecyclerViewExpandableItemManager(null)
        val dragAndDropManager = RecyclerViewDragDropManager().apply {
            setInitiateOnLongPress(true)
            setInitiateOnTouch(false)
            setInitiateOnMove(false)
        }
        var wrappedAdapter = expandableItemManager.createWrappedAdapter(adapter)
        wrappedAdapter = dragAndDropManager.createWrappedAdapter(wrappedAdapter)
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.get) {
                    SProjectAdapter.CHILD_ITEM_ID -> 1
                    SProjectAdapter.GROUP_ITEM_ID -> 2
                    else -> 2
                }
            }
        }
        content.apply {
            this.layoutManager = layoutManager
            adapter = wrappedAdapter
            addItemDecoration(ProjectsDivider())
        }
    }
}