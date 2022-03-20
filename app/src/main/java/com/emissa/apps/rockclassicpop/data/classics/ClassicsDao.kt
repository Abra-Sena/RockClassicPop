package com.emissa.apps.rockclassicpop.data.classics

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emissa.apps.rockclassicpop.model.Classic
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ClassicsDao {
    @Query("SELECT * from classic")
    fun getClassics() : Single<List<Classic>>

    @Query("SELECT * from classic ORDER BY trackId ASC")
    fun getClassicByIdAsc() : Single<List<Classic>>

    @Query("SELECT * from classic WHERE collectionName = :title")
    fun getSong(title: String) : Single<Classic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(classic: Classic) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSongs(classics: List<Classic>) : Completable
}