package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.rest.PopsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PopsPresenterImpl @Inject constructor(
    private val popsRepository: PopsRepository,
    private val disposables: CompositeDisposable
) : PopsPresenter {
    private var popSongContract: PopSongContract? = null

    override fun initializePresenter(viewContract: PopSongContract) {
        popSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        // not operable
    }

    override fun getPopMusics() {
        popSongContract?.loadingPopSongs(true)
        // Get responses from API call
        popsRepository.getPopSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { popRes -> popSongContract?.popSongsOnSuccess(popRes.pops)},
                { error -> popSongContract?.popSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }

    override fun destroyPresenter() {
        popSongContract = null
        disposables.dispose() //clear()
    }
}

interface PopsPresenter {
    fun initializePresenter(viewContract: PopSongContract)
    fun checkNetworkConnection()
    fun getPopMusics()
    fun destroyPresenter()
}

interface PopSongContract {
    fun loadingPopSongs(isLoading: Boolean)
    fun popSongsOnSuccess(pops: List<Pop>)
    fun popSongsOnError(error: Throwable)
}