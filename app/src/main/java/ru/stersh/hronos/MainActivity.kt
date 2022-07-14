package ru.stersh.hronos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.stersh.hronos.ui.project.list.ProjectsFragment

class MainActivity : AppCompatActivity() {

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
