package com.emissa.apps.rockclassicpop

import android.app.Application
import com.emissa.apps.rockclassicpop.data.classics.ClassicsDatabase
import com.emissa.apps.rockclassicpop.data.pops.PopsDatabase
import com.emissa.apps.rockclassicpop.data.rocks.RocksDatabase
import com.emissa.apps.rockclassicpop.di.ApplicationModule
import com.emissa.apps.rockclassicpop.di.DaggerMusicsComponent
import com.emissa.apps.rockclassicpop.di.MusicsComponent

class MusicApp : Application() {
    val classicDB: ClassicsDatabase by lazy {
        ClassicsDatabase.getClassicsDatabase(this)
    }

    val popDB: PopsDatabase by lazy {
        PopsDatabase.getPopsDatabase(this)
    }

    val rockDB: RocksDatabase by lazy {
        RocksDatabase.getRocksDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()

        musicsComponent = DaggerMusicsComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    companion object {
        lateinit var musicsComponent: MusicsComponent
    }
}