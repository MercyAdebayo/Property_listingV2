package com.example.homefinder.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.homefinder.utils.RetrofitInstance
import com.example.homefinder.model.UserInfoDto
import com.example.homefinder.databinding.ActivityAccountBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is logged in
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)

        if (token != null) {
            // Fetch and display user profile
            fetchUserProfile(token)
        } else {
            // Redirect to login if not logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Logout button logic
        binding.buttonLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun fetchUserProfile(token: String) {
        RetrofitInstance.authService.getUserInfo("Bearer $token").enqueue(object : Callback<UserInfoDto> {
            override fun onResponse(call: Call<UserInfoDto>, response: Response<UserInfoDto>) {
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    if (userInfo != null) {
                        displayUserProfile(userInfo)
                    }
                } else {
                    Toast.makeText(this@AccountActivity, "Failed to load user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserInfoDto>, t: Throwable) {
                Toast.makeText(this@AccountActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayUserProfile(userInfo: UserInfoDto) {
        binding.textUsername.text = userInfo.userName
        binding.textName.text = userInfo.fullName
        binding.textEmail.text = userInfo.email
        binding.textMobile.text = userInfo.mobile
    }

    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
