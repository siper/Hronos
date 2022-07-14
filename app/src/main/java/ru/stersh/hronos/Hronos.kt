package ru.stersh.hronos

import android.app.Application
import ru.stersh.hronos.core.Di

class Hronos : Application() {
    override fun onCreate() {
        super.onCreate()
        Di.init(this)
    }
}