package ru.stersh.hronos.feature.project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_projects.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di
import ru.stersh.hronos.feature.project.editor.EditorProjectDialog

class ProjectsFragment : MvpAppCompatFragment(R.layout.fragment_projects),
    ProjectsView {
    val presenter by moxyPresenter { ProjectsPresenter(Di.get()) }

    val adapter by lazy {
        ProjectsAdapter(
            onProjectClick = { project ->

            },
            onStartStopClick = { project ->
                presenter.onStartStopClick(project)
            }
        )
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
        content.layoutManager = GridLayoutManager(requireActivity(), 2)
        content.adapter = adapter
        content.addItemDecoration(ProjectsDivider())
    }

    override fun updateProjects(projects: List<UiProject>) {
        content.visibility = View.VISIBLE
        adapter.items = projects
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
}