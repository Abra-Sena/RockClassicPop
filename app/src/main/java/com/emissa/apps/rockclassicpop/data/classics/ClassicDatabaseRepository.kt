package com.emissa.apps.rockclassicpop.data.classics

import com.emissa.apps.rockclassicpop.model.Classic
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


interface ClassicDatabaseRepository {
    fun getAll() : Single<List<Classic>>
    fun insertSong(classic: Classic) : Completable
    fun insertAllSongs(classics: List<Classic>) : Completable
}

class ClassicDatabaseRepositoryImpl @Inject constructor (
    private val dao: ClassicsDao
) : ClassicDatabaseRepository {
    override fun getAll(): Single<List<Classic>> = dao.getClassics()

    override fun insertSong(classic: Classic): Completable = dao.insertSong(classic)

    override fun insertAllSongs(classics: List<Classic>): Completable = dao.insertAllSongs(classics)
}