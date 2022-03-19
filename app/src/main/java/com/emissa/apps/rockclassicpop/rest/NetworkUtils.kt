package com.emissa.apps.rockclassicpop.rest

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.subjects.BehaviorSubject

class NetworkUtils(
    private var context: Context? = null,
    private val connectivityManager: ConnectivityManager? =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
) : ConnectivityManager.NetworkCallback() {

    private val networkState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(isNetworkAvailable())

    fun registerForNetworkState() {
        connectivityManager?.registerNetworkCallback(networkRequest, this)
    }
    fun unregisterFromNetworkState() {
        connectivityManager?.unregisterNetworkCallback(this)
        context = null
    }

    private fun isNetworkAvailable(): Boolean {
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                return true
        }
        return false
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkState.onNext(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkState.onNext(false)
    }
}