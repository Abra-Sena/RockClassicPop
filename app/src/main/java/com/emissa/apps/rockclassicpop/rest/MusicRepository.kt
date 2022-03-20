package com.emissa.apps.rockclassicpop.rest

import com.emissa.apps.rockclassicpop.model.ClassicSongs
import com.emissa.apps.rockclassicpop.model.PopSongs
import com.emissa.apps.rockclassicpop.model.RockSongs
import io.reactivex.Single
import javax.inject.Inject

interface MusicRepository {
    fun getClassicSongs() : Single<ClassicSongs>
    fun getPopSongs() : Single<PopSongs>
    fun getRockSongs() : Single<RockSongs>
}


class MusicRepositoryImpl@Inject constructor(
    private val musicApi: MusicApi
) : MusicRepository {
    override fun getClassicSongs(): Single<ClassicSongs> {
        return musicApi.getClassicSongs()
    }

    override fun getPopSongs(): Single<PopSongs> {
        return musicApi.getPopSongs()
    }

    override fun getRockSongs(): Single<RockSongs> {
        return musicApi.getRockSongs()
    }
}