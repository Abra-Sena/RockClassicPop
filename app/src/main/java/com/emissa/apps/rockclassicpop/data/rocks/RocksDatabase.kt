package com.emissa.apps.rockclassicpop.data.rocks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emissa.apps.rockclassicpop.model.Rock

@Database(entities = [Rock::class], version = 1, exportSchema = false)
abstract class RocksDatabase : RoomDatabase() {
    abstract fun rocksDao() : RocksDao

    companion object {
        @Volatile
        private var INSTANCE: RocksDatabase? = null

        fun getRocksDatabase(context: Context): RocksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RocksDatabase::class.java,
                    "rocks_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}