package com.kaz.planets.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kaz.planets.R
import com.kaz.planets.databinding.ActivityDetailBinding
import com.kaz.planets.data.model.Planet

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val planet = intent.getSerializableExtra("planet") as? Planet

        planet?.let { p ->
            binding.textName.text = p.name
            binding.textDistance.text = getString(R.string.distance_sun_km, p.distance_from_sun)
            binding.textDiameter.text = getString(R.string.diameter_km, p.diameter)
            binding.textMoons.text = getString(R.string.moons_count, p.moons)
            binding.textRings.text = getString(
                if (p.has_rings) R.string.has_rings_yes else R.string.has_rings_no
            )
            binding.textPosition.text = getString(R.string.position_in_solar_system, p.id)




            Glide.with(this).load(p.image_url).into(binding.imagePlanetDetail)
        }
    }
}
