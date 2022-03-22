package com.emissa.apps.rockclassicpop

import android.app.Application
import com.emissa.apps.rockclassicpop.di.*
import java.text.DecimalFormatSymbols
import java.util.*

class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        musicsComponent = DaggerMusicsComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()

        // refresh listener to trigger new songs
    }

    companion object {
        var priceFormatted = DecimalFormatSymbols(Locale.getDefault()).currencySymbol
        lateinit var musicsComponent: MusicsComponent

        fun priceFormatted(price: String) = "$priceFormatted $price"

    }
}