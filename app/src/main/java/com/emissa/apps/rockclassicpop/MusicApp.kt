package com.emissa.apps.rockclassicpop

import android.app.Application
import com.emissa.apps.rockclassicpop.di.*

class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        musicsComponent = DaggerMusicsComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .presentersModule(PresentersModule())
            .networkModule(NetworkModule())
            .build()
    }

    companion object {
        lateinit var musicsComponent: MusicsComponent
    }
}