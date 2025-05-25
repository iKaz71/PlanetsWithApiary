package com.kaz.planets.data

import com.kaz.planets.data.model.Planet
import com.kaz.planets.data.remote.RetrofitClient

class PlanetRepository {
    suspend fun fetchPlanets(): List<Planet> {
        val response = RetrofitClient.apiService.getPlanets()
        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            emptyList()
        }
    }


}
