package com.emissa.apps.rockclassicpop.data.rocks

import com.emissa.apps.rockclassicpop.model.Rock
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface RockDatabaseRepository {
    fun getAll() : Single<List<Rock>>
    fun insertSong(rock: Rock) : Completable
    fun insertAllSongs(rocks: List<Rock>) : Completable
}

class RockDatabaseRepositoryImpl @Inject constructor (
    private val dao: RocksDao
) : RockDatabaseRepository {
    override fun getAll(): Single<List<Rock>> = dao.getRocks()

    override fun insertSong(rock: Rock): Completable = dao.insertSong(rock)

    override fun insertAllSongs(rocks: List<Rock>): Completable = dao.insertAllSongs(rocks)
}