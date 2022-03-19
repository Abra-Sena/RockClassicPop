package com.emissa.apps.rockclassicpop.rest

import okhttp3.Interceptor
import okhttp3.Response

class MusicRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//            .newBuilder().apply {
//            addHeader("ClassicHead", "ClassicHeader")
//            addHeader("RockHead", "RockHeader")
//            addHeader("PopHead", "PopHeader")
//        }.build()

        return chain.proceed(request)
    }
}