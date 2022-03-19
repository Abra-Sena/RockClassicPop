package com.emissa.apps.rockclassicpop.views

import android.media.MediaPlayer
import androidx.fragment.app.Fragment

class BaseFragment : Fragment() {
//    private lateinit var mActivity:

    fun playSong() {
        val player = MediaPlayer()
        player.stop()
        player.reset()
        // add song url here
//        player.setDataSource()
        player.prepare()
        player.start()
    }
}