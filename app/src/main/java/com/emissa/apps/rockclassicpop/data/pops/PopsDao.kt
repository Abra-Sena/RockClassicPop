package com.emissa.apps.rockclassicpop.data.pops

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emissa.apps.rockclassicpop.model.Pop
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PopsDao {
    @Query("SELECT * from pop")
    fun getPops() : Single<List<Pop>>

    @Query("SELECT * from pop ORDER BY trackId ASC")
    fun getPopSongsByIdAsc() : Single<List<Pop>>

    @Query("SELECT * from pop WHERE collectionName = :title")
    fun getSong(title: String) : Single<Pop>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(pop: Pop) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSongs(pop: List<Pop>) : Completable
}