package com.emissa.apps.rockclassicpop.views

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

open class BaseFragment : Fragment() {

    fun playSong(musicUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(musicUrl), "audio/*")
        }
        startActivity(intent)
    }

    fun toastMessageOnFetch(listSize: Int) = Toast.makeText(
        requireContext(),
        "Found $listSize Results",
        Toast.LENGTH_LONG
    ).show()

    fun toastMessageOffline(listSize: Int, genre: String) = Toast.makeText(
        requireContext(),
        "Found $listSize Saved $genre Songs",
        Toast.LENGTH_LONG
    ).show()

    fun showAlertDialog(error: Throwable) = AlertDialog.Builder(requireContext())
        .setTitle("An Error Occurred!")
        .setMessage(error.localizedMessage)
        .setPositiveButton("DISMISS") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        .create()
        .show()
}