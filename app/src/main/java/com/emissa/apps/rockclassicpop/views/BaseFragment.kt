package com.emissa.apps.rockclassicpop.views

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Open class that all 3 views are implementing
 * Contains methods the 3 fragments are sharing
 */
open class BaseFragment : Fragment() {

    /**
     * Using implicit intent to play song on device's default media player
     */
    fun playSong(musicUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(musicUrl), "audio/*")
        }
        startActivity(intent)
    }

    /**
     * Shows a toast message informing user about how many songs were retrieve from server
     */
    fun toastMessageOnFetch(listSize: Int) = Toast.makeText(
        requireContext(),
        "Found $listSize Results",
        Toast.LENGTH_LONG
    ).show()

    /**
     * Shows a toast message informing user that the displayed songs are coming from database
     * because there no internet connection is available
     */
    fun toastMessageOffline(listSize: Int, genre: String) = Toast.makeText(
        requireContext(),
        "Found $listSize Saved $genre Songs",
        Toast.LENGTH_LONG
    ).show()

    /**
     * Alerts user on error occurring while fetching data from server
     */
    fun showErrorAlertDialog(error: Throwable) = AlertDialog.Builder(requireContext())
        .setTitle("An Error Occurred!")
        .setMessage(error.localizedMessage)
        .setPositiveButton("OK") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        .create()
        .show()

    /**
     * Alerts user that data can not be retrieved because device is offline
     */
    fun showNoInternetAlertDialog() = AlertDialog.Builder(requireContext())
        .setTitle("No Internet Connection!")
        .setMessage("No internet connection available. Can't load more songs!")
        .setPositiveButton("OK") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        .create()
        .show()
}