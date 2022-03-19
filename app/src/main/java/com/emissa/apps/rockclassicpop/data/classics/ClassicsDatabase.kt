package com.emissa.apps.rockclassicpop.data.classics

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emissa.apps.rockclassicpop.model.Classic

@Database(entities = [Classic::class], version = 1, exportSchema = false)
abstract class ClassicsDatabase : RoomDatabase() {
    abstract fun classicsDao() : ClassicsDao

    companion object {
        @Volatile
        private var INSTANCE: ClassicsDatabase? = null

        fun getClassicsDatabase(context: Context): ClassicsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClassicsDatabase::class.java,
                    "classics_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}