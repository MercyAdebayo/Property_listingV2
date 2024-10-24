package com.example.homefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var usernameInput: EditText
    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Find views by ID
        usernameInput = findViewById(R.id.input_username)
        fullNameInput = findViewById(R.id.input_fullname)
        emailInput = findViewById(R.id.input_email)
        mobileInput = findViewById(R.id.input_mobile)
        passwordInput = findViewById(R.id.input_password)
        signupButton = findViewById(R.id.button_signup)

        // Set signup button click listener
        signupButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val fullName = fullNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val mobile = mobileInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate inputs
            if (username.isNotEmpty() && fullName.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty()) {
                // Call the signup API using Retrofit
                signupUser(username, fullName, email, mobile, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to sign up the user via the API
    private fun signupUser(username: String, fullName: String, email: String, mobile: String, password: String) {
        val signupRequest = RegisterRequest(username, fullName, mobile, email, password)

        RetrofitInstance.authService.signupUser(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "Signup successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SignupError", "Error Response: $errorBody")
                    Toast.makeText(this@SignupActivity, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
