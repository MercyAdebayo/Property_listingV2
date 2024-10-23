package com.example.homefinder

import android.content.Intent

import android.os.Bundle

import android.widget.Button

import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.example.homefinder.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Initialize View Binding

        binding = ActivityAccountBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Check if user is logged in

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        val token = sharedPreferences.getString("TOKEN", null)

        if (token != null) {

            // User is logged in, show the profile

            displayUserProfile()

        } else {

            // User is not logged in, redirect to login page

            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)

            finish() // Close this activity

        }

        // Logout button logic

        binding.buttonLogout.setOnClickListener {

            logoutUser()

        }

    }

    // Function to display the user profile

    private fun displayUserProfile() {

        // Here you can show user profile information

        // For now, just show a placeholder text

        binding.textUsername.text = "Welcome, [User]"

        binding.textEmail.text = "user@example.com"

        // You can fetch and display more user data if available

    }

    // Function to log the user out

    private fun logoutUser() {

        // Clear the token from SharedPreferences

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.clear() // Clear all saved data

        editor.apply()

        // Redirect the user to the login screen after logout

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)

        finish() // Close this activity

    }

}

