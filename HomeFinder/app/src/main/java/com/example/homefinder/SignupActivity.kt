package com.example.homefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.telecom.Call
import android.widget.Button
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignupActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameInput = findViewById(R.id.input_name)
        emailInput = findViewById(R.id.input_email)
        passwordInput = findViewById(R.id.input_password)
        mobileInput = findViewById(R.id.input_mobile)
        signupButton = findViewById(R.id.button_signup)

        signupButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val mobile = mobileInput.text.toString().trim()

            // Call the signup API using Retrofit
            signupUser(name, email, password, mobile)
        }
    }

    private fun signupUser(name: String, email: String, password: String, mobile: String) {
        // Retrofit call for signup
        val service = Retrofit.Builder()
            .baseUrl("http://your-django-backend-url/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)

        val signupRequest = SignupRequest(name,password )

        // Send signup request
        service.signupUser(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    // Handle successful signup (e.g., redirect to the main screen)
                } else {
                    // Handle failed signup
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                // Handle failure (e.g., network error)
            }
        })
    }
}

