package ru.stersh.hronos.feature.project.list

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.hronos.feature.project.list.data.SectionsRepositoryImpl
import ru.stersh.hronos.feature.project.list.data.TaskRepositoryImpl
import ru.stersh.hronos.feature.project.list.domain.SectionsRepository
import ru.stersh.hronos.feature.project.list.domain.TaskRepository
import ru.stersh.hronos.feature.project.list.ui.ProjectsAdapterDataProvider
import ru.stersh.hronos.feature.project.list.ui.ProjectsViewModel

val projectListModule = module {
    single<SectionsRepository> { SectionsRepositoryImpl(get(), get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single { ProjectsAdapterDataProvider(get()) }
    viewModel { ProjectsViewModel(get(), get()) }
}