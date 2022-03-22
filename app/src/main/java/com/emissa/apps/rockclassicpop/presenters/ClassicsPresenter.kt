package com.emissa.apps.rockclassicpop.presenters

import com.emissa.apps.rockclassicpop.data.classics.ClassicDatabaseRepository
import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.rest.ClassicsRepository
import com.emissa.apps.rockclassicpop.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Constructor injection to implement the presenter of Classic songs
 */
class ClassicsPresenterImpl @Inject constructor(
    private val classDatabaseRepo: ClassicDatabaseRepository,
    private val classicRepository: ClassicsRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposables: CompositeDisposable
) : ClassicsPresenter {
    private var classicSongContract: ClassicSongContract? = null

    /**
     * Initialize Classic songs presenter
     */
    override fun initializePresenter(viewContract: ClassicSongContract) {
        classicSongContract = viewContract
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
                        // handles offline data retrieve
                        retrieveFromDatabaseOffline()
                    }
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    classicSongContract?.classicSongsOnError(error)
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
        classicSongContract = null
        disposables.clear()
    }

    /**
     * Retrieves data
     */
    private fun retrieveSongsViaNetwork() {
        classicRepository.getClassicSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { classicsRes ->
                    saveRetrievedSongs(classicsRes.classics)
                },
                { error ->
                    retrieveFromDatabaseOffline()
                    classicSongContract?.classicSongsOnError(error)
                }
            )
            .apply {
                disposables.add(this)
            }
    }

    /**
     * Save data saved to database
     */
    private fun saveRetrievedSongs(classics: List<Classic>) {
        classDatabaseRepo.insertAllSongs(classics)
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
    }

    /**
     * Retrieves data saved to database while offline
     */
    private fun retrieveFromDatabaseOffline() {
        classDatabaseRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { classRes -> classicSongContract?.loadSongsOffline(classRes) },
                { error -> classicSongContract?.classicSongsOnError(error) }
            )
            .apply {
                disposables.add(this)
            }
    }
}

/**
 * Interface to present view for Classic songs
 */
interface ClassicsPresenter {
    fun initializePresenter(viewContract: ClassicSongContract)
    fun checkNetworkConnection()
    fun getClassicMusics()
    fun destroyPresenter()
}

interface ClassicSongContract {
    fun loadingClassicSongs(isLoading: Boolean)
    fun loadSongsOffline(classics: List<Classic>)
    fun classicSongsOnSuccess(classics: List<Classic>)
    fun classicSongsOnError(error: Throwable)
}