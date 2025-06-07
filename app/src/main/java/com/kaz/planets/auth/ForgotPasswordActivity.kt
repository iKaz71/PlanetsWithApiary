package com.kaz.planets.auth

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kaz.planets.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val resetPasswordButton = findViewById<Button>(R.id.resetPasswordButton)
        val loginTextView = findViewById<TextView>(R.id.loginTextView)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, getString(R.string.recovery_mail_sent), Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, getString(R.string.recovery_mail_error), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            }
        }

        loginTextView.setOnClickListener {
            finish()
        }
    }
}
