package com.emissa.apps.rockclassicpop.model


import com.google.gson.annotations.SerializedName

data class PopSongs(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val pops: List<Pop>
)