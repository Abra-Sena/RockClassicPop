package com.emissa.apps.rockclassicpop.views

import android.media.MediaPlayer
import android.os.Handler
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private lateinit var mediaPlayer: MediaPlayer

    fun playSong(musicUrl: String) {
        mediaPlayer = MediaPlayer().apply {
            stop()
            reset()
            setDataSource(musicUrl)
            prepare()
            start()
        }
    }
}