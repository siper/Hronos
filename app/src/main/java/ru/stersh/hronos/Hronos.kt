package ru.stersh.hronos

import android.app.Application

class Hronos : Application() {
    override fun onCreate() {
        super.onCreate()
        Di.init(this)
    }
}