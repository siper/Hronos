package ru.stersh.hronos.core.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.stersh.hronos.R
import ru.stersh.hronos.core.data.category.Category

/**
 * Класс для хранения вспомогательных методов и миграций для базы данных
 * @param context - контекст для доступа к ресурсам приложения
 * @param db - база данных
 */
class DBHelper(private val context: Context, private val db: SupportSQLiteDatabase) {
    /**
     * Создание категории "Избранное"
     */
    fun createFavoriteCategory() {
        val favoriteTitle = context.getString(R.string.favorite_title)
        val favoriteId = Category.FAVORITE_ID
        db.execSQL("INSERT INTO `categories` (id, title) VALUES ($favoriteId, '$favoriteTitle')")
    }

    /**
     * Создание категории "Входящие"
     */
    fun createIncomingCategory() {
        val incomingTitle = context.getString(R.string.incoming_title)
        val incomingId = Category.INCOMING_ID
        db.execSQL("INSERT INTO `categories` (id, title) VALUES ($incomingId, '$incomingTitle')")
    }
}