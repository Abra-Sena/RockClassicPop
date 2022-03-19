package com.emissa.apps.rockclassicpop.model


import com.google.gson.annotations.SerializedName


data class ClassicSongs(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val classics: List<Classic>
)