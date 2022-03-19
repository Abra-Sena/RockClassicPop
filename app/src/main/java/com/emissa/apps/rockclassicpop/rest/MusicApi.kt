package com.emissa.apps.rockclassicpop.rest

import com.emissa.apps.rockclassicpop.model.ClassicSongs
import com.emissa.apps.rockclassicpop.model.PopSongs
import com.emissa.apps.rockclassicpop.model.RockSongs
import io.reactivex.Single
import retrofit2.http.GET

interface MusicApi {
    @GET(CLASSICS_URL)
    fun getClassicSongs(): Single<ClassicSongs>

    @GET(POPS_URL)
    fun getPopSongs(): Single<PopSongs>

    @GET(ROCKS_URL)
    fun getRockSongs(): Single<RockSongs>

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val CLASSICS_URL = "search?term=classick&amp;media=music&amp;entity=song&amp;limit=50"
        const val POPS_URL = "search?term=pop&amp;media=music&amp;entity=song&amp;limit=50"
        const val ROCKS_URL = "search?term=rock&amp;media=music&amp;entity=song&amp;limit=50"
    }
}