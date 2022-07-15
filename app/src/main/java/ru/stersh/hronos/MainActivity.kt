package ru.stersh.hronos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.root_container,
                ru.stersh.hronos.feature.project.list.ui.ProjectsFragment()
            )
            .commit()
    }
}
