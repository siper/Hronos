package ru.stersh.hronos.core

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.KoinComponent
import org.koin.dsl.module
import ru.stersh.hronos.R
import ru.stersh.hronos.core.data.HronosDB
import ru.stersh.hronos.feature.category.core.Category
import ru.stersh.hronos.feature.project.core.ProjectsInteractor

object Di : KoinComponent {
    val modules by lazy { listOf(interactor, data, repository) }

    private val interactor = module {
        single { ProjectsInteractor(get(), get(), get()) }
    }

    private val data = module {
        single {
            Room
                .databaseBuilder(
                    androidApplication(),
                    HronosDB::class.java,
                    "hronos-database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        // Создание категории "Входящие"
                        val incomingTitle = androidApplication().getString(R.string.incoming_title)
                        val incomingId = Category.INCOMING_ID
                        db.execSQL("INSERT INTO `categories` (id, title) VALUES ($incomingId, $incomingTitle)")
                        // Создание категории "Избранное"
                        val favoriteTitle = androidApplication().getString(R.string.favorite_title)
                        val favoriteId = Category.FAVORITE_ID
                        db.execSQL("INSERT INTO `categories` (id, title) VALUES ($favoriteId, $favoriteTitle)")
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