package com.example.homefinder.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homefinder.utils.RetrofitInstance
import com.example.homefinder.adapters.HomeAdapter
import com.example.homefinder.databinding.ActivitySearchBinding
import com.example.homefinder.model.PropertyListDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var homeAdapter: HomeAdapter
    private var searchResults: List<PropertyListDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the RecyclerView for search results
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)

        // Handle search icon click
        binding.iconSearch.setOnClickListener {
            val query = binding.inputSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchProperties(query)  // Trigger search when the icon is clicked
            } else {
                Toast.makeText(this, "Please enter property name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to search properties
    private fun searchProperties(query: String) {
        RetrofitInstance.propertyApiService.searchProperties(query).enqueue(object : Callback<List<PropertyListDto>> {
            override fun onResponse(call: Call<List<PropertyListDto>>, response: Response<List<PropertyListDto>>) {
                if (response.isSuccessful) {
                    searchResults = response.body() ?: emptyList()
                    Log.d("SearchActivity", "Search results: $searchResults")

                    // Check if results are empty
                    if (searchResults.isEmpty()) {
                        Toast.makeText(this@SearchActivity, "No properties found", Toast.LENGTH_SHORT).show()
                    }

                    // Initialize the adapter with the search results
                    homeAdapter = HomeAdapter(searchResults) { property ->
                        val intent = Intent(this@SearchActivity, PropertyDetailActivity::class.java)
                        intent.putExtra("PROPERTY_ID", property.id)
                        startActivity(intent)
                    }

                    binding.recyclerViewSearchResults.apply {
                        adapter = homeAdapter
                        layoutManager = LinearLayoutManager(this@SearchActivity)
                    }
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

