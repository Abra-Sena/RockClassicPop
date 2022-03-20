package com.emissa.apps.rockclassicpop.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.emissa.apps.rockclassicpop.data.classics.ClassicDatabaseRepository
import com.emissa.apps.rockclassicpop.data.pops.PopDatabaseRepository
import com.emissa.apps.rockclassicpop.data.rocks.RockDatabaseRepository
import com.emissa.apps.rockclassicpop.presenters.*
import com.emissa.apps.rockclassicpop.rest.*
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class PresentersModule {

    @Provides
    fun provideNetworkMonitor(
        context: Context,
        networkRequest: NetworkRequest,
        connectivityManager: ConnectivityManager
    ) : NetworkMonitor = NetworkMonitor(
        context,
        networkRequest,
        connectivityManager
    )

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideClassicsPresenter(
        classicDatabaseRepo: ClassicDatabaseRepository,
        classicsRepository: ClassicsRepository,
        networkMonitor: NetworkMonitor,
        disposable: CompositeDisposable
    ) : ClassicsPresenter {
        return ClassicsPresenterImpl(
            classicDatabaseRepo,
            classicsRepository,
            networkMonitor,
            disposable
        )
    }

    @Provides
    fun providePopsPresenter(
        popDatabaseRepo: PopDatabaseRepository,
        popsRepository: PopsRepository,
        networkMonitor: NetworkMonitor,
        disposable: CompositeDisposable
    ) : PopsPresenter {
        return PopsPresenterImpl(
            popDatabaseRepo,
            popsRepository,
            networkMonitor,
            disposable
        )
    }

    @Provides
    fun provideRocksPresenter(
        rockDatabaseRepo: RockDatabaseRepository,
        rocksRepository: RocksRepository,
        networkMonitor: NetworkMonitor,
        disposable: CompositeDisposable
    ) : RocksPresenter {
        return RocksPresenterImpl(
            rockDatabaseRepo,
            rocksRepository,
            networkMonitor,
            disposable
        )
    }
}