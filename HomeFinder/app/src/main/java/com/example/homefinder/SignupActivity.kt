package com.example.homefinder

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import android.widget.Button

import android.widget.EditText

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText

    private lateinit var passwordInput: EditText

    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        // Find views by ID

        usernameInput = findViewById(R.id.input_username)

        passwordInput = findViewById(R.id.input_password)

        signupButton = findViewById(R.id.button_signup)

        // Set signup button click listener

        signupButton.setOnClickListener {

            val username = usernameInput.text.toString().trim()

            val password = passwordInput.text.toString().trim()

            // Call the signup API using Retrofit

            signupUser(username, password)

        }

    }

    // Function to sign up the user

    private fun signupUser(username: String, password: String) {

        // Create Retrofit instance and call service

        val service = Retrofit.Builder()

            .baseUrl("http://your-backend-url/api/")  // Replace with your backend URL

            .addConverterFactory(GsonConverterFactory.create())

            .build()

            .create(AuthService::class.java)

        // Prepare the signup request with only username and password

        val signupRequest = SignupRequest(username, password)

        // Send signup request using Retrofit

        service.signupUser(signupRequest).enqueue(object : Callback<SignupResponse> {

            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {

                if (response.isSuccessful) {

                    // Handle successful signup (e.g., redirect to the main screen)

                } else {

                    // Handle signup failure (e.g., show error message)

                }

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {

                // Handle failure (e.g., network error)

            }

        })

    }

}

