package ru.stersh.hronos.core

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.KoinComponent
import org.koin.dsl.module
import ru.stersh.hronos.core.data.DBHelper
import ru.stersh.hronos.core.data.HronosDB
import ru.stersh.hronos.feature.project.core.ProjectsInteractor

object Di : KoinComponent {
    val modules by lazy { listOf(interactor, data, repository) }

    private val interactor = module {
        single { ProjectsInteractor(get(), get(), get()) }
    }

    private val data = module {
        single {
            Room
                .databaseBuilder(androidApplication(), HronosDB::class.java, HronosDB.NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        DBHelper(androidApplication(), db).apply {
                            createIncomingCategory()
                            createFavoriteCategory()
                        }
                    }
                })
                .build()
        }
    }

    private val repository = module {
        single { get<HronosDB>().projectDao() }
        single { get<HronosDB>().taskDao() }
        single { get<HronosDB>().categoryDao() }
    }
}