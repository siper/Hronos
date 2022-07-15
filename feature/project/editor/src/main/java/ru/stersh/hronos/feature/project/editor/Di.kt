package ru.stersh.hronos.feature.project.editor

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.hronos.feature.project.editor.data.AddProjectRepositoryImpl
import ru.stersh.hronos.feature.project.editor.data.CategorySuggestionsRepositoryImpl
import ru.stersh.hronos.feature.project.editor.domain.AddProjectRepository
import ru.stersh.hronos.feature.project.editor.domain.CategorySuggestionsRepository
import ru.stersh.hronos.feature.project.editor.presentation.ProjectEditorViewModel

val projectEditorModule = module {
    single<CategorySuggestionsRepository> { CategorySuggestionsRepositoryImpl(get()) }
    single<AddProjectRepository> { AddProjectRepositoryImpl(get(), get()) }
    viewModel { ProjectEditorViewModel(get(), get()) }
}