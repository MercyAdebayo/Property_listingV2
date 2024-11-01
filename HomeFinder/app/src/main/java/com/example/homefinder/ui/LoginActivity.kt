package com.example.homefinder.ui

import android.content.Intent

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.homefinder.model.LoginReqDto
import com.example.homefinder.model.LoginResDto
import com.example.homefinder.utils.RetrofitInstance

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

        // Handle login button click
        binding.loginButton.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Register text click
        binding.textRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to handle the login logic
    private fun loginUser(username: String, password: String) {
        val loginReqDto = LoginReqDto(username, password)
        // Make the login request using Retrofit
        RetrofitInstance.authService.loginUser(loginReqDto).enqueue(object : Callback<LoginResDto> {
            override fun onResponse(call: Call<LoginResDto>, response: Response<LoginResDto>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Save the token in SharedPreferences
                        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("TOKEN", it.token)
                        editor.putString("USERNAME", it.userName)
                        val expirationTime = System.currentTimeMillis() + (60 * 60 * 1000)
                        editor.putLong("TOKEN_EXPIRATION", expirationTime)

                        editor.apply()

                        // Navigate to the MainActivity after successful login
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    }

                } else {
                    // Show login failure message
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResDto>, t: Throwable) {
                // Handle network or request failure
                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

