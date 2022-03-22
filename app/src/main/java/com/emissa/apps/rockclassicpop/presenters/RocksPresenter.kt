package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.data.rocks.RockDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.rest.RocksRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Constructor injection to implement the presenter of Classic songs
 */
class RocksPresenterImpl @Inject constructor(
    private val rockDatabaseRepo: RockDatabaseRepository,
    private val rocksRepository: RocksRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : RocksPresenter {
    private var rockSongContract: RockSongContract? = null

    /**
     * Initialize Rock songs presenter
     */
    override fun initializePresenter(viewContract: RockSongContract) {
        rockSongContract = viewContract
    }

    /**
     * Checks network/internet availability
     */
    override fun checkNetworkConnection() {
        networkMonitor.registerNetworkMonitor()
    }

    /**
     * Returns rock songs
     * Fetch data from server if network is available
     * Retrieve data saved to database otherwise
     */
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

    /**
     * Destroys the presenter
     */
    override fun destroyPresenter() {
        rockSongContract = null
        disposables.clear() //clear()
    }

    /**
     * Retrieves data
     */
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

    /**
     * Save data saved to database
     */
    private fun saveRetrievedSongs(rocks: List<Rock>) {
        rockDatabaseRepo.insertAllSongs(rocks)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { retrieveFromDatabase() }
            .apply {
                disposables.add(this)
            }
    }

    /**
     * Retrieves data saved to database
     */
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

    /**
     * Retrieves data saved to database while offline
     */
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

/**
 * Interface to present view for Rock songs
 */
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