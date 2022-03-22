package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.data.rocks.RockDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.rest.RocksRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RocksPresenterImpl @Inject constructor(
    private val rockDatabaseRepo: RockDatabaseRepository,
    private val rocksRepository: RocksRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : RocksPresenter {
    private var rockSongContract: RockSongContract? = null

    override fun initializePresenter(viewContract: RockSongContract) {
        rockSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        networkMonitor.registerNetworkMonitor()
    }

    override fun getRockMusics() {
        rockSongContract?.loadingRockSongs(true)

        //Get responses from API call
        networkMonitor.networkState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { networkState ->
                    if (networkState) {
                        retrieveSongsViaNetwork()
                    } else {
                        // handle offline data retrieve here
                        retrieveFromDatabaseOffline()
                    }
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    rockSongContract?.rockSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
    }

    override fun destroyPresenter() {
        rockSongContract = null
        disposables.clear() //clear()
    }

    private fun retrieveSongsViaNetwork() {
        rocksRepository.getRockSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rockRes ->
                    saveRetrievedSongs(rockRes.rocks)
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    rockSongContract?.rockSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
    }

    private fun saveRetrievedSongs(rocks: List<Rock>) {
        rockDatabaseRepo.insertAllSongs(rocks)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { retrieveFromDatabase() }
            .apply {
                disposables.add(this)
            }
    }

    private fun retrieveFromDatabase() {
        rockDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rockRes -> rockSongContract?.rockSongsOnSuccess(rockRes) },
                { error -> rockSongContract?.rockSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }

    private fun retrieveFromDatabaseOffline() {
        rockDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rockRes -> rockSongContract?.loadSongsOffline(rockRes) },
                { error -> rockSongContract?.rockSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
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
    fun loadSongsOffline(rocks: List<Rock>)
    fun rockSongsOnSuccess(rocks: List<Rock>)
    fun rockSongsOnError(error: Throwable)
}