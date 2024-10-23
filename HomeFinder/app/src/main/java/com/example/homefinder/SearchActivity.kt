package com.example.homefinder


import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.homefinder.databinding.ActivitySearchBinding

import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var homeAdapter: HomeAdapter

    private var searchResults: List<PropertyListDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Initialize View Binding

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Set up the RecyclerView for search results

        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)

        // Handle search button click

        binding.buttonSearch.setOnClickListener {

            val query = binding.inputSearch.text.toString().trim()

            if (query.isNotEmpty()) {

                searchProperties(query)  // Perform the search

            } else {

                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()

            }

        }

    }

    // Function to search properties

    private fun searchProperties(query: String) {

        RetrofitInstance.propertyApiService.searchProperties(query).enqueue(object : Callback<List<PropertyListDto>> {

            override fun onResponse(call: Call<List<PropertyListDto>>, response: Response<List<PropertyListDto>>) {

                if (response.isSuccessful) {

                    searchResults = response.body() ?: emptyList()

                    homeAdapter = HomeAdapter(searchResults)

                    binding.recyclerViewSearchResults.adapter = homeAdapter

                } else {

                    Toast.makeText(this@SearchActivity, "No results found", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<List<PropertyListDto>>, t: Throwable) {

                Toast.makeText(this@SearchActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()

            }

        })

    }

}

