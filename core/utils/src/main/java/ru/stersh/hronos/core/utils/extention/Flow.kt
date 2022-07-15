package ru.stersh.hronos.core.utils.extention

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<List<T>>.mapItems(crossinline block: suspend (item: T) -> R): Flow<List<R>> {
    return map { list ->
        list.map { item ->
            block.invoke(item)
        }
    }
}