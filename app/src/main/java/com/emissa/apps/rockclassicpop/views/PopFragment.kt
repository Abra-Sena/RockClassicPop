package com.emissa.apps.rockclassicpop.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.R
import com.emissa.apps.rockclassicpop.di.DaggerMusicsComponent

class PopFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        MusicApp.musicsComponent.inject(this)
        DaggerMusicsComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop, container, false)
    }

    companion object {
        fun newInstance() = PopFragment()
    }
}