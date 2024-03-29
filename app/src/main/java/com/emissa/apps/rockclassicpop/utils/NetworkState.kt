package com.emissa.apps.rockclassicpop.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

object NetworkState {
    val observeNetworkState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
}

class NetworkMonitor @Inject constructor(
    private var context: Context?,
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build(),
    private val connectivityManager: ConnectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
) : ConnectivityManager.NetworkCallback() {

    val networkState: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(isNetworkAvailable())

    private fun isNetworkAvailable(): Boolean {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
        }
        return false
    }

    fun registerNetworkMonitor() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
        NetworkState.observeNetworkState.onNext(isNetworkAvailable())
    }
    fun unregisterNetworkMonitor() {
        connectivityManager.unregisterNetworkCallback(this)
        context = null
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        NetworkState.observeNetworkState.onNext(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        NetworkState.observeNetworkState.onNext(false)
    }
}