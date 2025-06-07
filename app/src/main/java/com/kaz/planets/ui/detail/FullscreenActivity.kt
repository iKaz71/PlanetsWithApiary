package com.kaz.planets.ui.detail


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kaz.planets.auth.LoginActivity
import com.kaz.planets.databinding.ActivityFullscreenBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding

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

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoId = intent.getStringExtra("videoId") ?: return
        val youtubePlayerView = binding.youtubePlayerFullscreen
        lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                player.loadVideo(videoId, 0f)
            }
        })
    }
}
