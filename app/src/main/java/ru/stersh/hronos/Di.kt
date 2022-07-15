package ru.stersh.hronos

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stersh.hronos.core.room.roomModule
import ru.stersh.hronos.feature.project.editor.projectEditorModule
import ru.stersh.hronos.feature.project.list.navigation.ProjectListNavigation
import ru.stersh.hronos.feature.project.list.projectListModule
import ru.stersh.hronos.navigation.ProjectListNavigationImpl

object Di : KoinComponent {

    private val core = listOf(roomModule)
    private val features = listOf(projectListModule, projectEditorModule)
    private val navigation = module {
        single<ProjectListNavigation> { ProjectListNavigationImpl() }
    }

    fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(core)
            modules(features)
            modules(navigation)
        }
    }
}