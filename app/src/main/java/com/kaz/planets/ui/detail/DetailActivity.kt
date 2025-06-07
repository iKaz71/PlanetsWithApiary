package com.kaz.planets.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.kaz.planets.R
import com.kaz.planets.auth.LoginActivity
import com.kaz.planets.data.model.Planet
import com.kaz.planets.databinding.ActivityDetailBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var googleMap: GoogleMap

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val planet = intent.getSerializableExtra("planet") as? Planet

        planet?.let { p ->
            Log.d("AUDIO_DEBUG", "Planet recibido: ${p.name}, audioEffect = ${p.audioEffect}")

            binding.textName.text = p.name
            binding.textDistance.text = getString(R.string.distance_sun_km, p.distance_from_sun)
            binding.textDiameter.text = getString(R.string.diameter_km, p.diameter)
            binding.textMoons.text = getString(R.string.moons_count, p.moons)
            binding.textRings.text = getString(
                if (p.has_rings) R.string.has_rings_yes else R.string.has_rings_no
            )
            binding.textPosition.text = getString(R.string.position_in_solar_system, p.id)

            Glide.with(this).load(p.image_url).into(binding.imagePlanetDetail)

            val soundName = p.audioEffect.replace(".mp3", "")
            val soundResId = resources.getIdentifier(soundName, "raw", packageName)

            if (soundResId != 0) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this, soundResId)
                mediaPlayer?.start()
            }

            lifecycle.addObserver(binding.youtubePlayerView)

            val videoId = p.videoUrl.substringAfter("v=")

            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val uiController = DefaultPlayerUiController(binding.youtubePlayerView, youTubePlayer)
                    binding.youtubePlayerView.setCustomPlayerUi(uiController.rootView)

                    uiController.showFullscreenButton(true)
                    uiController.setFullScreenButtonClickListener {
                        val intent = Intent(this@DetailActivity, FullscreenActivity::class.java)
                        intent.putExtra("videoId", videoId)
                        startActivity(intent)
                    }

                    youTubePlayer.cueVideo(videoId, 0f)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
                ) {
                    if (state == com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState.PLAYING) {
                        mediaPlayer?.pause()
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }
                }
            })
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val planet = intent.getSerializableExtra("planet") as? Planet
        planet?.let {
            val location = LatLng(it.latitude, it.longitude)

            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.planeta))
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
