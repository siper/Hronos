package ru.stersh.hronos.core

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stersh.hronos.core.data.DBHelper
import ru.stersh.hronos.core.data.HronosDB
import ru.stersh.hronos.feature.category.CategoryInteractor
import ru.stersh.hronos.feature.project.ProjectInteractor
import ru.stersh.hronos.feature.task.TaskInteractor

object Di : KoinComponent {
    private val modules by lazy { listOf(interactor, data, repository) }

    fun init(application: Application) {
        startKoin {
            androidContext(application)
            modules(modules)
        }
    }

    private val interactor = module {
        single { ProjectInteractor(get(), get(), get()) }
        single { TaskInteractor(get()) }
        single { CategoryInteractor(get()) }
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