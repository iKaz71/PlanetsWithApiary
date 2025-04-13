package com.kaz.planets.data.remote

import com.kaz.planets.data.remote.PlanetApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.kaz.planets.BuildConfig

object RetrofitClient {

    val apiService: PlanetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanetApiService::class.java)
    }
}
