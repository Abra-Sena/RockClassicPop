package com.emissa.apps.rockclassicpop.di

import com.emissa.apps.rockclassicpop.MainActivity
import com.emissa.apps.rockclassicpop.views.ClassicFragment
import com.emissa.apps.rockclassicpop.views.PopFragment
import com.emissa.apps.rockclassicpop.views.RockFragment
import dagger.Component

@Component(
    modules = [
        ApplicationModule::class,
        PresentersModule::class,
        NetworkModule::class
    ]
)
interface MusicsComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(classicFragment: ClassicFragment)
    fun inject(popFragment: PopFragment)
    fun inject(rockFragment: RockFragment)
}