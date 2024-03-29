package com.emissa.apps.rockclassicpop.di

import android.util.Log
import com.emissa.apps.rockclassicpop.rest.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        Log.d("Network module", "Providing logging interceptor")
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideMusicInterceptor() = MusicRequestInterceptor()

    @Provides
    fun provideOKHttpClient(
        musicRequestInterceptor: MusicRequestInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        Log.d("Network module", "Providing Ok Http client")
        return OkHttpClient.Builder()
            .addInterceptor(musicRequestInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient, gson: Gson) : MusicApi {
        Log.d("Network module", "Providing Retrofit")
        return Retrofit.Builder()
            .baseUrl(MusicApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MusicApi::class.java)
    }

    @Provides
    fun provideClassicsRepository(musicApi: MusicApi): ClassicsRepository {
        Log.d("Network module", "Providing classic songs repository")
        return ClassicsRepositoryImpl(musicApi)
    }
    @Provides
    fun provideRocksRepository(musicApi: MusicApi): RocksRepository {
        Log.d("Network module", "Providing rock songs repository")
        return RocksRepositoryImpl(musicApi)
    }
    @Provides
    fun providePopsRepository(musicApi: MusicApi): PopsRepository {
        Log.d("Network module", "Providing pop songs repository")
        return PopsRepositoryImpl(musicApi)
    }
}