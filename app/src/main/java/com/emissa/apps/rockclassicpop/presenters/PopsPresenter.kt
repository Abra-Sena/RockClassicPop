package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.data.pops.PopDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.rest.PopsRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Constructor injection to implement the presenter of Classic songs
 */
class PopsPresenterImpl @Inject constructor(
    private val popDatabaseRepo: PopDatabaseRepository,
    private val popsRepository: PopsRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : PopsPresenter {
    private var popSongContract: PopSongContract? = null

    /**
     * Initialize Pop songs presenter
     */
    override fun initializePresenter(viewContract: PopSongContract) {
        popSongContract = viewContract
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
                        retrieveFromDatabaseOffline()
                    }
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    popSongContract?.popSongsOnError(error)
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
        popSongContract = null
        disposables.clear()
    }

    /**
     * Retrieves data
     */
    private fun retrieveSongsViaNetwork() {
        popsRepository.getPopSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { popRes ->
                    saveRetrievedSongs(popRes.pops)
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    popSongContract?.popSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
    }

    /**
     * Save data saved to database
     */
    private fun saveRetrievedSongs(pops: List<Pop>) {
        popDatabaseRepo.insertAllSongs(pops)
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

    /**
     * Retrieves data saved to database while offline
     */
    private fun retrieveFromDatabaseOffline() {
        popDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { popsRes -> popSongContract?.loadSongsOffline(popsRes) },
                { error -> popSongContract?.popSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }
}

/**
 * Interface to present view for Classic songs
 */
interface PopsPresenter {
    fun initializePresenter(viewContract: PopSongContract)
    fun checkNetworkConnection()
    fun getPopMusics()
    fun destroyPresenter()
}

interface PopSongContract {
    fun loadingPopSongs(isLoading: Boolean)
    fun loadSongsOffline(pops: List<Pop>)
    fun popSongsOnSuccess(pops: List<Pop>)
    fun popSongsOnError(error: Throwable)
}