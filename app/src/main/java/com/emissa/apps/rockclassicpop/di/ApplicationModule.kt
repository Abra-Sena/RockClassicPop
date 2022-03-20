package com.emissa.apps.rockclassicpop.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.room.Room
import com.emissa.apps.rockclassicpop.data.classics.ClassicDatabaseRepository
import com.emissa.apps.rockclassicpop.data.classics.ClassicDatabaseRepositoryImpl
import com.emissa.apps.rockclassicpop.data.classics.ClassicsDao
import com.emissa.apps.rockclassicpop.data.classics.ClassicsDatabase
import com.emissa.apps.rockclassicpop.data.pops.PopsDao
import com.emissa.apps.rockclassicpop.data.pops.PopsDatabase
import com.emissa.apps.rockclassicpop.data.rocks.RocksDao
import com.emissa.apps.rockclassicpop.data.rocks.RocksDatabase
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(
    private val applicationContext: Context
) {
    @Provides
    fun provideApplicationContext() = applicationContext

    @Provides
    fun provideConnectivityManager(context: Context) : ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideNetworkRequest() : NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    @Provides
    fun provideClassicDatabase(context: Context) : ClassicsDatabase {
        return ClassicsDatabase.getClassicsDatabase(context)
    }

    @Provides
    fun provideRockDatabase(context: Context) : RocksDatabase {
        return RocksDatabase.getRocksDatabase(context)
    }

    @Provides
    fun providePopDatabase(context: Context) : PopsDatabase {
        return PopsDatabase.getPopsDatabase(context)
    }

    @Provides
    fun provideClassDao(database: ClassicsDatabase) : ClassicsDao = database.classicsDao()

    @Provides
    fun provideRockDao(database: RocksDatabase) : RocksDao = database.rocksDao()

    @Provides
    fun providePopDao(database: PopsDatabase) : PopsDao = database.popsDao()

    @Provides
    fun provideClassDataRepo(
        dao: ClassicsDao
    ) : ClassicDatabaseRepository = ClassicDatabaseRepositoryImpl(dao)

//    @Provides
//    fun
//    @Provides
//    fun
}