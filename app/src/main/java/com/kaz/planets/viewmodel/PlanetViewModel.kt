package com.kaz.planets.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kaz.planets.data.PlanetRepository
import com.kaz.planets.data.model.Planet
import kotlinx.coroutines.launch

class PlanetViewModel : ViewModel() {

    private val repository = PlanetRepository()

    private val _planets = MutableLiveData<List<Planet>>()
    val planets: LiveData<List<Planet>> = _planets

    init {
        fetchPlanets()
    }

    fun fetchPlanets() {
        viewModelScope.launch {
            try {
                val data = repository.fetchPlanets()
                Log.d("PlanetViewModel", "Planetas obtenidos: ${data.size}")
                _planets.postValue(data)
            } catch (e: Exception) {
                Log.e("PlanetViewModel", "Error al obtener planetas", e)
                _planets.postValue(emptyList())
            }
        }
    }
}
