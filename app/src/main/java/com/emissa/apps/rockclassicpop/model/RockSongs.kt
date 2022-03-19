package com.emissa.apps.rockclassicpop.model


import com.google.gson.annotations.SerializedName

data class RockSongs(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val rocks: List<Rock>
)