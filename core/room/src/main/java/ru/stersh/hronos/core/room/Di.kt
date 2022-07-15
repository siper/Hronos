package ru.stersh.hronos.core.room

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module {
    single {
        Room
            .databaseBuilder(androidApplication(), HronosDB::class.java, HronosDB.NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<HronosDB>().projectDao() }
    single { get<HronosDB>().taskDao() }
    single { get<HronosDB>().categoryDao() }
}