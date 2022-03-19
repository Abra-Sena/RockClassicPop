package com.emissa.apps.rockclassicpop.di

import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.emissa.apps.rockclassicpop.presenters.*
import com.emissa.apps.rockclassicpop.rest.*
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class PresentersModule {
    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideClassicsPresenter(
        classicsRepository: ClassicsRepository,
        disposable: CompositeDisposable
    ) : ClassicsPresenter {
        return ClassicsPresenterImpl(classicsRepository, disposable)
    }

    @Provides
    fun providePopsPresenter(
        popsRepository: PopsRepository,
        disposable: CompositeDisposable
    ) : PopsPresenter {
        return PopsPresenterImpl(popsRepository, disposable)
    }

    @Provides
    fun provideRocksPresenter(
        rocksRepository: RocksRepository,
        disposable: CompositeDisposable
    ) : RocksPresenter {
        return RocksPresenterImpl(rocksRepository, disposable)
    }
}