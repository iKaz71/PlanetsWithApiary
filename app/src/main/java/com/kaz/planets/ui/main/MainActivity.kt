package com.kaz.planets.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaz.planets.databinding.ActivityMainBinding
import com.kaz.planets.ui.detail.DetailActivity
import com.kaz.planets.ui.adapter.PlanetAdapter
import com.kaz.planets.viewmodel.PlanetViewModel
import com.kaz.planets.R
import com.google.firebase.auth.FirebaseAuth
import com.kaz.planets.auth.LoginActivity
import com.kaz.planets.utils.NetworkMonitor
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var messages: List<String>
    private var messageIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var messageRunnable: Runnable

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PlanetViewModel by viewModels()

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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que quieres cerrar sesión?")
                .setPositiveButton("Sí") { dialog, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        messages = listOf(
            getString(R.string.loading_msg_1),
            getString(R.string.loading_msg_2),
            getString(R.string.loading_msg_3),
            getString(R.string.loading_msg_4),
            getString(R.string.loading_msg_5),
            getString(R.string.loading_msg_6),
            getString(R.string.loading_msg_7),
            getString(R.string.loading_msg_8),
            getString(R.string.loading_msg_9),
            getString(R.string.loading_msg_10),
            getString(R.string.loading_msg_11),
            getString(R.string.loading_msg_12),
            getString(R.string.loading_msg_13),
            getString(R.string.loading_msg_14),
            getString(R.string.loading_msg_15),
            getString(R.string.loading_msg_16),
            getString(R.string.loading_msg_17),
            getString(R.string.loading_msg_18),
            getString(R.string.loading_msg_19)
        )

        messageRunnable = object : Runnable {
            override fun run() {
                val randomMessage = messages.random()
                binding.loadingMessage.text = randomMessage
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(messageRunnable)

        val adapter = PlanetAdapter { planet ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("planet", planet)
            startActivity(intent)
        }

        binding.recyclerViewPlanets.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerViewPlanets.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPlanets.adapter = adapter

        viewModel.planets.observe(this) { planets ->
            if (planets.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_data_error), Toast.LENGTH_LONG).show()
            } else {
                adapter.setData(planets)
                binding.recyclerViewPlanets.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.loadingMessage.visibility = View.GONE
                handler.removeCallbacks(messageRunnable)
            }
        }

        NetworkMonitor.isConnected.observe(this) { connected ->
            if (connected) {
                Snackbar.make(binding.root, "Conexión restablecida. Recargando...", Snackbar.LENGTH_SHORT).show()
                viewModel.fetchPlanets()
            } else {
                Snackbar.make(binding.root, "Sin conexión a Internet", Snackbar.LENGTH_INDEFINITE).show()
            }
        }
    }
}
