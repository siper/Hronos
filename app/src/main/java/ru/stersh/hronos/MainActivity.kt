package ru.stersh.hronos

import android.os.Bundle
import ru.stersh.hronos.core.ui.BaseActivity
import ru.stersh.hronos.feature.project.main.ProjectsFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.root_container,
                ProjectsFragment()
            )
            .commit()
    }
}
