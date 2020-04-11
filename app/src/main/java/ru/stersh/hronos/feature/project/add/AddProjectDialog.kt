package ru.stersh.hronos.feature.project.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_add_project.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import org.koin.core.get
import ru.stersh.hronos.R
import ru.stersh.hronos.core.Di

class AddProjectDialog : MvpBottomSheetDialogFragment(), AddProjectView {
    private val presenter by moxyPresenter { AddProjectPresenter(Di.get()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.setOnClickListener { presenter.addProject(project_title.text.toString()) }
    }

    override fun done() {
        dismiss()
    }

    override fun showError() {
        project_input_layout.error = getString(R.string.project_name_error)
    }
}