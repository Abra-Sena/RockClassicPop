package com.emissa.apps.rockclassicpop.data

import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.model.Rock

interface MusicItemClicked {
    fun onClassicSongClicked(classic: Classic)
    fun onPopSongClicked(pop: Pop)
    fun onRockSongClicked(rock: Rock)
}