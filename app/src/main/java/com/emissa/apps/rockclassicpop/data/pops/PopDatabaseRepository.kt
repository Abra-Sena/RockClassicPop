package com.emissa.apps.rockclassicpop.data.pops

import com.emissa.apps.rockclassicpop.model.Pop
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface PopDatabaseRepository {
    fun getAll() : Single<List<Pop>>
    fun insertSong(pop: Pop) : Completable
    fun insertAllSongs(pops: List<Pop>) : Completable
}

class PopDatabaseRepositoryImpl @Inject constructor (
    private val dao: PopsDao
) : PopDatabaseRepository {
    override fun getAll(): Single<List<Pop>> = dao.getPops()

    override fun insertSong(pop: Pop): Completable = dao.insertSong(pop)

    override fun insertAllSongs(pops: List<Pop>): Completable = dao.insertAllSongs(pops)
}