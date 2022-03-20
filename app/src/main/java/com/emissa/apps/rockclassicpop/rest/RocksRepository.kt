package com.emissa.apps.rockclassicpop.rest

import com.emissa.apps.rockclassicpop.model.RockSongs
import io.reactivex.Single
import javax.inject.Inject

interface RocksRepository {
    fun getRockSongs() : Single<RockSongs>
}

class RocksRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi
) : RocksRepository {
    override fun getRockSongs(): Single<RockSongs> {
        return musicApi.getRockSongs()
    }
}