package com.emissa.apps.rockclassicpop.presenters

import android.util.Log
import com.emissa.apps.rockclassicpop.data.classics.ClassicDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.rest.ClassicsRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ClassicsPresenterImpl @Inject constructor(
    private val classDatabaseRepo: ClassicDatabaseRepository,
    private val classicRepository: ClassicsRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : ClassicsPresenter {
    private var classicSongContract: ClassicSongContract? = null

    override fun initializePresenter(viewContract: ClassicSongContract) {
        classicSongContract = viewContract
    }

    override fun checkNetworkConnection() {
        networkMonitor.registerNetworkMonitor()
    }

    override fun getClassicMusics() {
        classicSongContract?.loadingClassicSongs(true)
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
                        Log.d("Classic Presenter Impl", "Issue with network state, it is: $networkState")
                    }
                },
                { error ->
                    classicSongContract?.classicSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
        Log.d("Classics Presenter Impl", "Retrieving classic songs completed. Musics loaded")
    }

    override fun destroyPresenter() {
        classicSongContract = null
        disposables.dispose() // try with clear() in case of error
    }

    private fun retrieveSongsViaNetwork() {
        Log.d("Classics Presenter Impl", "Retrieving classic songs...")
        classicRepository.getClassicSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { classicsRes -> saveRetrievedSongs(classicsRes.classics) },
                { error -> classicSongContract?.classicSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
        Log.d("Classics Presenter Impl", "Classic songs retrieve completed!")
    }
    private fun saveRetrievedSongs(classics: List<Classic>) {
        Log.d("Classics Presenter Impl", "Saving retrieved classic songs...")
        classDatabaseRepo.insertAllSongs(classics)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { retrieveFromDatabase() },
                { Log.e("Classic Presenter Impl", "Error saving retrieved songs, $it.toString()") }
            )
            .apply {
                disposables.add(this)
            }
        Log.d("Classics Presenter Impl", "Classic songs saving completed!")
    }

    private fun retrieveFromDatabase() {
        Log.d("Classics Presenter Impl", "Retrieving from room database...")
        classDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { classicsRes -> classicSongContract?.classicSongsOnSuccess(classicsRes) },
                { error -> classicSongContract?.classicSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
        Log.d("Classics Presenter Impl", "Retrieve from room database completed!")
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