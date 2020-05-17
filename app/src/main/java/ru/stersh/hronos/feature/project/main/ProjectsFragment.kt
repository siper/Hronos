package ru.stersh.hronos.feature.project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_projects.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di
import ru.stersh.hronos.feature.category.UiCategory
import ru.stersh.hronos.feature.project.editor.EditorProjectDialog

class ProjectsFragment : MvpAppCompatFragment(R.layout.fragment_projects), ProjectsView {
    private val presenter by moxyPresenter { ProjectsPresenter(Di.get()) }

    private val adapter by lazy { SectionedRecyclerViewAdapter() }

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

    override fun updateSections(sections: List<ProjectSection>) {
        content.visibility = View.VISIBLE
        adapter.removeAllSections()
        sections.forEach {
            adapter.addSection(it)
        }
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

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        content.apply {
            this.layoutManager = layoutManager
            this.adapter = this@ProjectsFragment.adapter
            addItemDecoration(ProjectsDivider())
        }
    }
}