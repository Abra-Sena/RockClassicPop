package com.emissa.apps.rockclassicpop.data.rocks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.model.Rock
import io.reactivex.Single

@Dao
interface RocksDao {
    @Query("SELECT * from rock")
    fun getRocks() : Single<List<Rock>>

    @Query("SELECT * from rock ORDER BY trackId ASC")
    fun getRockSongsByIdAsc() : Single<List<Rock>>

    @Query("SELECT * from rock WHERE collectionName = :title")
    fun getSong(title: String) : Single<Rock>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(rock: Rock)
}