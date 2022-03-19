package com.emissa.apps.rockclassicpop.data.pops

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emissa.apps.rockclassicpop.model.Pop

@Database(entities = [Pop::class], version = 1, exportSchema = false)
abstract class PopsDatabase : RoomDatabase() {
    abstract fun popsDao() : PopsDao

    companion object {
        @Volatile
        private var INSTANCE: PopsDatabase? = null

        fun getPopsDatabase(context: Context): PopsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PopsDatabase::class.java,
                    "pops_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}