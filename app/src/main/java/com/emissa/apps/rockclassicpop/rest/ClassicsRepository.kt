package com.emissa.apps.rockclassicpop.rest

import com.emissa.apps.rockclassicpop.model.ClassicSongs
import io.reactivex.Single
import javax.inject.Inject

interface ClassicsRepository {
    fun getClassicSongs() : Single<ClassicSongs>
}

class ClassicsRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi
) : ClassicsRepository {

    override fun getClassicSongs(): Single<ClassicSongs> {
        return musicApi.getClassicSongs()
    }
}