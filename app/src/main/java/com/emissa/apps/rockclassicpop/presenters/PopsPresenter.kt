package com.emissa.apps.rockclassicpop.presenters

import android.util.Log
import com.emissa.apps.rockclassicpop.data.pops.PopDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.rest.PopsRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PopsPresenterImpl @Inject constructor(
    private val popDatabaseRepo: PopDatabaseRepository,
    private val popsRepository: PopsRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : PopsPresenter {
    private var popSongContract: PopSongContract? = null

    override fun initializePresenter(viewContract: PopSongContract) {
        popSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        networkMonitor.registerNetworkMonitor()
    }

    override fun getPopMusics() {
        popSongContract?.loadingPopSongs(true)

        // Get responses from API call
        networkMonitor.networkState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { networkState ->
                    if (networkState) {
                        retrieveSongsViaNetwork()
                    } else {
                        // handle offline data retrieve here
                        Log.d("Pop Presenter Impl", "Issue with network state, it is: $networkState")
                    }
                },
                { error ->
                    popSongContract?.popSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
    }

    override fun destroyPresenter() {
        popSongContract = null
        disposables.dispose() //clear()
    }

    private fun retrieveSongsViaNetwork() {
        popsRepository.getPopSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { popRes -> saveRetrievedSongs(popRes.pops) },
                { error -> popSongContract?.popSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }

    private fun saveRetrievedSongs(pops: List<Pop>) {
        popDatabaseRepo.insertAllSongs(pops)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrieveFromDatabase() },
                { Log.e("Pop Presenter Impl", "Error saving retrieved songs, $it.toString()") }
            )
            .apply {
                disposables.add(this)
            }
    }

    private fun retrieveFromDatabase() {
        popDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rockRes -> popSongContract?.popSongsOnSuccess(rockRes) },
                { error -> popSongContract?.popSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
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