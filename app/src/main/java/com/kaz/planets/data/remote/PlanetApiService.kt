package com.kaz.planets.data.remote

import com.kaz.planets.data.model.Planet
import retrofit2.Response
import retrofit2.http.GET

interface PlanetApiService {
    @GET("planets")
    suspend fun getPlanets(): Response<List<Planet>>
}
