package ru.stersh.hronos.ui.project.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.fragment_projects.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di
import ru.stersh.hronos.ui.project.editor.EditorProjectDialog

class ProjectsFragment : MvpAppCompatFragment(R.layout.fragment_projects), ProjectsView {
    private val presenter by moxyPresenter { ProjectsPresenter(Di.get(), get(), get()) }

    private val dataProvider by inject<ProjectsAdapterDataProvider>()

    private val adapter by lazy {
        ProjectsAdapter(dataProvider) { presenter.onStartStopClick(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(savedInstanceState)
    }

    override fun updateSections(sections: List<UiProjectSection>) {
        content.visibility = View.VISIBLE
        dataProvider.setData(sections)
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
            presenter.stopRunningTasks()
        }
    }

    private fun initAdapter(savedInstanceState: Bundle?) {
        val lm = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)


        val savedState = savedInstanceState?.getParcelable<Parcelable>(PREVIOUS_SAVE_STATE)
        val expMgr = RecyclerViewExpandableItemManager(savedState)
//        expMgr?.setOnGroupCollapseListener(this)
//        expMgr?.setOnGroupExpandListener(this)

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

        with(content) {
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