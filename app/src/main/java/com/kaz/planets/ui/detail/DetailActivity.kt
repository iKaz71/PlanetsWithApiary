package com.kaz.planets.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kaz.planets.R
import com.kaz.planets.auth.LoginActivity
import com.kaz.planets.databinding.ActivityDetailBinding
import com.kaz.planets.data.model.Planet
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var mediaPlayer: MediaPlayer? = null

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

            if (!p.audioEffect.isNullOrEmpty()) {
                val soundName = p.audioEffect.replace(".mp3", "")
                val soundResId = resources.getIdentifier(soundName, "raw", packageName)

                if (soundResId != 0) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this, soundResId)
                    mediaPlayer?.start()
                }
            }

            // Reproducimos video con YouTubePlayerView
            val youtubePlayerView = binding.youtubePlayerView
            lifecycle.addObserver(youtubePlayerView)

            if (!p.videoUrl.isNullOrEmpty()) {
                val videoId = p.videoUrl.substringAfter("v=")

                youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val uiController = DefaultPlayerUiController(youtubePlayerView, youTubePlayer)
                        youtubePlayerView.setCustomPlayerUi(uiController.rootView)

                        uiController.showFullscreenButton(true)
                        uiController.setFullScreenButtonClickListener {
                            // Lanzamos actividad de pantalla completa
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
                            mediaPlayer?.let {
                                if (it.isPlaying) {
                                    it.pause()
                                    it.release()
                                    mediaPlayer = null
                                    Log.d("AUDIO_DEBUG", "Audio local detenido porque el video comenzo")
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
