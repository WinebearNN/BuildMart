package com.versaiilers.buildmart.data.network.apiService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    const val BASE_URL = "http://10.0.2.2:8080" // Замените на ваш базовый URL


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiServiceUser: ApiServiceUser by lazy {
        retrofit.create(ApiServiceUser::class.java)
    }

    val apiServiceAdvertisement:ApiServiceAdvertisement by lazy {
        retrofit.create(ApiServiceAdvertisement::class.java)
    }

    val apiServiceChat: ApiServiceChat by lazy {
        retrofit.create(ApiServiceChat::class.java)
    }

    val apiServiceMessage: ApiServiceMessage by lazy {
        retrofit.create(ApiServiceMessage::class.java)
    }

    val webSocketClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()  // Создаем экземпляр OkHttpClient через builder
    }
}

