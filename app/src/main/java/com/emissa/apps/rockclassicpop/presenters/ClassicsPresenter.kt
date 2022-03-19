package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.model.ClassicSongs
import com.emissa.apps.rockclassicpop.rest.ClassicsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassicsPresenterImpl @Inject constructor(
    private val classicRepository: ClassicsRepository,
    private val disposables: CompositeDisposable
) : ClassicsPresenter {
    private var classicSongContract: ClassicSongContract? = null

    override fun initializePresenter(viewContract: ClassicSongContract) {
        classicSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        // not operable
    }

    override fun getClassicMusics() {
        classicSongContract?.loadingClassicSongs(true)
        // Get responses from API call
        classicRepository.getClassicSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { classicsRes -> classicSongContract?.classicSongsOnSuccess(classicsRes.classics) },
                { error -> classicSongContract?.classicSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }

    override fun destroyPresenter() {
        classicSongContract = null
        disposables.dispose() // try with clear() in case of error
    }
}

interface ClassicsPresenter {
    fun initializePresenter(viewContract: ClassicSongContract)
    fun checkNetworkConnection()
    fun getClassicMusics()
    fun destroyPresenter()
}

interface ClassicSongContract {
    fun loadingClassicSongs(isLoading: Boolean)
    fun classicSongsOnSuccess(classics: List<Classic>)
    fun classicSongsOnError(error: Throwable)
}