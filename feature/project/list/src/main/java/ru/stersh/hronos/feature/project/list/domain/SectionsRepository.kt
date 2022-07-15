package ru.stersh.hronos.feature.project.list.domain

import kotlinx.coroutines.flow.Flow

internal interface SectionsRepository {
    fun getSections(): Flow<List<Section>>
}