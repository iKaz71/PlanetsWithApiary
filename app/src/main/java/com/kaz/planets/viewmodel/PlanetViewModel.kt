package com.kaz.planets.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kaz.planets.data.PlanetRepository
import kotlinx.coroutines.Dispatchers

class PlanetViewModel : ViewModel() {

    private val repository = PlanetRepository()

    val planets = liveData(Dispatchers.IO) {
        try {
            val data = repository.fetchPlanets()
            Log.d("PlanetViewModel", "Planetas obtenidos: ${data.size}")
            emit(data)
        } catch (e: Exception) {
            Log.e("PlanetViewModel", "Error al obtener planetas", e)
            emit(emptyList())
        }
    }

}
