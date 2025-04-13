package com.kaz.planets.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaz.planets.databinding.ItemPlanetBinding
import com.kaz.planets.data.model.Planet

class PlanetAdapter(
    private val onClick: (Planet) -> Unit
) : RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {

    private var planets: List<Planet> = emptyList()

    inner class PlanetViewHolder(private val binding: ItemPlanetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planet: Planet) {
            binding.textPlanetName.text = planet.name
            Glide.with(binding.imagePlanet.context)
                .load(planet.image_url)
                .into(binding.imagePlanet)

            binding.root.setOnClickListener { onClick(planet) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val binding = ItemPlanetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bind(planets[position])
    }

    override fun getItemCount() = planets.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Planet>) {
        planets = newList
        notifyDataSetChanged()
    }
}

