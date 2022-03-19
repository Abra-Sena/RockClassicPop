package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.rest.RocksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RocksPresenterImpl @Inject constructor(
    private val rocksRepository: RocksRepository,
    private val disposables: CompositeDisposable
) : RocksPresenter {
    private var rockSongContract: RockSongContract? = null

    override fun initializePresenter(viewContract: RockSongContract) {
        rockSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        // not operable
    }

    override fun getRockMusics() {
        rockSongContract?.loadingRockSongs(true)
        //Get responses from API call
        rocksRepository.getRockSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rockRes -> rockSongContract?.rockSongsOnSuccess(rockRes.rocks)},
                { error -> rockSongContract?.rockSongsOnError(error) }
            )
            .apply {
                disposables.addAll(this)
            }
    }

    override fun destroyPresenter() {
        rockSongContract = null
        disposables.dispose() //clear()
    }
}

interface RocksPresenter {
    fun initializePresenter(viewContract: RockSongContract)
    fun checkNetworkConnection()
    fun getRockMusics()
    fun destroyPresenter()
}

interface RockSongContract {
    fun loadingRockSongs(isLoading: Boolean)
    fun rockSongsOnSuccess(rocks: List<Rock>)
    fun rockSongsOnError(error: Throwable)
}