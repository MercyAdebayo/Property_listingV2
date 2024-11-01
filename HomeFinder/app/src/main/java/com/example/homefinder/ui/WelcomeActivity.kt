package com.example.homefinder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.homefinder.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val loginButton = findViewById<Button>(R.id.button_login)
        val signupButton = findViewById<Button>(R.id.button_signup)
        val skipButton = findViewById<TextView>(R.id.button_skip)

        loginButton.setOnClickListener {
            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            // Redirect to SignupActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        skipButton.setOnClickListener {
            // Redirect to the main listing page (Guests can browse but not save or chat)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

