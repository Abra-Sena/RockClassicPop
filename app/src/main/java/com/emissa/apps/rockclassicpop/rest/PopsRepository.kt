package com.emissa.apps.rockclassicpop.rest

import com.emissa.apps.rockclassicpop.model.ClassicSongs
import com.emissa.apps.rockclassicpop.model.PopSongs
import io.reactivex.Single
import javax.inject.Inject

interface PopsRepository {
    fun getPopSongs() : Single<PopSongs>
}

class PopsRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi
) : PopsRepository {
    override fun getPopSongs(): Single<PopSongs> {
        return musicApi.getPopSongs()
    }
}