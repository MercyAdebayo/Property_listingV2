package com.example.homefinder


import android.content.Intent

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.example.homefinder.databinding.ActivityLoginBinding

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loginButton.setOnClickListener {

            val username = binding.inputUsername.text.toString().trim() // Username input

            val password = binding.inputPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                loginUser(username, password)

            } else {

                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun loginUser(username: String, password: String) {

        val loginReqDto = LoginReqDto(username, password) // Use LoginReqDto here

        RetrofitInstance.authService.loginUser(loginReqDto).enqueue(object : Callback<LoginResDto> {

            override fun onResponse(call: Call<LoginResDto>, response: Response<LoginResDto>) {

                if (response.isSuccessful) {

                    val loginResponse = response.body()

                    loginResponse?.let {

                        // Save token to SharedPreferences for future authenticated requests

                        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

                        val editor = sharedPreferences.edit()

                        editor.putString("TOKEN", it.token)

                        editor.apply()

                        // Navigate to the main screen

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                        finish()  // End login activity

                    }

                } else {

                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<LoginResDto>, t: Throwable) {

                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()

            }

        })

    }

}

