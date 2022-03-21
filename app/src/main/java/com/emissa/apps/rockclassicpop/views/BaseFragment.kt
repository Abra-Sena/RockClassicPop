package com.emissa.apps.rockclassicpop.views

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun playSong(musicUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(musicUrl), "audio/*")
        }

        startActivity(intent)
    }
}