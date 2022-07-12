package ru.stersh.hronos

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import ru.stersh.hronos.core.Di

class Hronos : Application() {
    override fun onCreate() {
        super.onCreate()
        Di.init(this)
        initThreeTeen()
        initCalligraphy()
    }

    private fun initThreeTeen() {
        AndroidThreeTen.init(this)
    }

    private fun initCalligraphy() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Montserrat-Medium.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}