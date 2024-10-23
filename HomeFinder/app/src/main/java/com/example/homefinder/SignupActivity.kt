package com.example.homefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Find views by ID (binding UI elements to variables)
        usernameInput = findViewById(R.id.input_username)
        passwordInput = findViewById(R.id.input_password)
        signupButton = findViewById(R.id.button_signup)

        // Set signup button click listener
        signupButton.setOnClickListener {
            // Get the input values for username and password
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Ensure inputs are not empty before making the signup request
            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Call the signup API using Retrofit
                signupUser(username, password)
            } else {
                // Display a message if the inputs are invalid
                Toast.makeText(this, "Please enter a valid username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to sign up the user via the API
    private fun signupUser(username: String, password: String) {
        // Use the RetrofitInstance to access the authService
        val signupRequest = LoginReqDto(username, password)

        // Make the asynchronous network call using authService
        RetrofitInstance.authService.signupUser(signupRequest).enqueue(object : Callback<SignupResponse> {

            // Handle the response (this is called if the request is successful, even if the server returns an error)
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    // Signup succeeded
                    Toast.makeText(this@SignupActivity, "Signup successful!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                } else {
                    // Signup failed but the server responded (e.g., validation error)
                    Toast.makeText(this@SignupActivity, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            // Handle failure (this is called if there is a network failure or an unexpected error)
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                // Handle network error or other issues
                Toast.makeText(this@SignupActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
