package com.example.homefinder

import android.content.Intent

import android.os.Bundle

import android.util.Log

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.homefinder.databinding.ActivityMainBinding

import com.google.android.material.bottomnavigation.BottomNavigationView

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeAdapter: HomeAdapter

    private var homeList: List<PropertyListDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Initialize View Binding

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Set up the RecyclerView using View Binding

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up BottomNavigationView

        val bottomNavigationView = binding.bottomNavigation

        // Set Home as selected by default

        bottomNavigationView.selectedItemId = R.id.navigation_home

        // Handle bottom navigation item clicks

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_home -> {

                    // Already on the home page, do nothing

                    true

                }

                R.id.navigation_notifications -> {

                    // Navigate to the Notifications page

                    startActivity(Intent(this, NotificationsActivity::class.java))

                    true

                }

                R.id.navigation_favorite -> {

                    // Navigate to the Favorites page

                    startActivity(Intent(this, FavoritesActivity::class.java))

                    true

                }

                R.id.navigation_account -> {

                    // Navigate to the Account page

                    startActivity(Intent(this, AccountActivity::class.java))

                    true

                }

                R.id.navigation_search -> {

                    // Navigate to the Search page

                    startActivity(Intent(this, SearchActivity::class.java))

                    true

                }

                else -> false

            }

        }

        // Fetch properties from API

        fetchProperties()

    }

    private fun fetchProperties() {

        RetrofitInstance.propertyApiService.getPropertyList(1).enqueue(object : Callback<List<PropertyListDto>> {

            override fun onResponse(call: Call<List<PropertyListDto>>, response: Response<List<PropertyListDto>>) {
                if (response.isSuccessful) {
                    homeList = response.body() ?: emptyList()
                    homeAdapter = HomeAdapter(homeList)
                    binding.recyclerView.adapter = homeAdapter
                } else {
                    // Log more information about the error
                    Log.e("MainActivity", "Error loading properties: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Failed to load properties. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<PropertyListDto>>, t: Throwable) {

                Log.e("MainActivity", "Failed to fetch properties: ${t.message}")

            }

        })

    }

}

