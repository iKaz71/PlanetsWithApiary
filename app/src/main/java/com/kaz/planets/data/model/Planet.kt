package com.kaz.planets.data.model

import java.io.Serializable

data class Planet(
    val id: Int,
    val name: String,
    val distance_from_sun: Double,
    val diameter: Int,
    val moons: Int,
    val has_rings: Boolean,
    val image_url: String,
    val videoUrl: String,
    val audioEffect: String
) : Serializable
